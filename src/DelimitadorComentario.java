public class DelimitadorComentario {
    private enum Estado {
        EstadoInicial,
        EstadoA,
        EstadoB,
        EstadoC,
        EstadoD,
    }

    /** ---- Retorno do Lexema Analisado ---- **/

    public String getLexema(String texto, int posicao) { // Estado Inicial
        String lexema = ""; // Lexema que irá ser retornado
        int estado = 0; // Estado do Autômato

        try{
            for (int i = posicao; i < texto.length(); i++) {
                char c = texto.charAt(i);
                if (c == '/') {
                    lexema = estadoInicial(texto, i, lexema, estado);
                    break;
                }
                if(i == texto.length() - 1)
                    throw new RuntimeException("Não é um comentario");
            }

            return lexema;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    /** ---- Estados ---- **/

    private String estadoInicial(String texto, int posicao, String lexema, int estado){
        char c = texto.charAt(posicao);
        if( c == '/' ){
            estado = Estado.EstadoInicial.ordinal();
            return proximoEstado(texto, posicao, lexema, estado);
        }
        else if(texto.charAt(posicao - 1) == '*'){
            return estadoFinal(lexema);
        }


        throw new RuntimeException("Error de execução");
    }

    private String estadoA(String texto, int posicao, String lexema, int estado){
        char c = texto.charAt(posicao);

        if( c == '*' && texto.charAt(posicao - 1) == '/' ){
            return proximoEstado(texto, posicao, lexema, estado);
        }
        else if( c == '/' && texto.charAt(posicao - 1) == '/' ){
            estado = Estado.EstadoD.ordinal();
            return proximoEstado(texto, posicao, lexema, estado);
        }
        else{
            relatorErro();
        }

        throw new RuntimeException("Error de execução");
    }

    private String estadoB(String texto, int posicao, String lexema, int estado){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == '*') {
                return proximoEstado(texto, i, lexema, estado);
            }
            else{
                lexema = mesmoEstado(texto, i, lexema);
            }
        }
        throw new RuntimeException("Error de execução");
    }

    private String estadoC(String texto, int posicao, String lexema, int estado){
        char c = texto.charAt(posicao);

        if( c == '/' && texto.charAt(posicao - 1) == '*') {
            return proximoEstado(texto, posicao, lexema, estado);
        }
        else {
            return estadoAnterior(texto, posicao, lexema, estado);
        }
    }

    private String estadoD(String texto, int posicao, String lexema, int estado){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == 'n' && texto.charAt(i - 1) == '\\') {
                estado = 3;
                return proximoEstado(texto, i, lexema, estado);
            }
            else{
                lexema = mesmoEstado(texto, i, lexema);
            }
        }
        throw new RuntimeException("Error de execução");
    }

    private String estadoFinal(String lexema){
        return lexema;
    }

    /** ---- Logica entre os Estados ---- **/

    private String proximoEstado(String texto, int posicao, String lexema, int estado){
        char c = texto.charAt(posicao);
        lexema += c;

        if(estado == Estado.EstadoInicial.ordinal()){
            estado++;
            return estadoA(texto, posicao + 1, lexema, estado);
        }
        else if(estado == Estado.EstadoA.ordinal()){
            estado++;
            return estadoB(texto, posicao + 1, lexema, estado);
        }
        else if(estado == Estado.EstadoB.ordinal()){
            estado++;
            return estadoC(texto, posicao + 1, lexema, estado);
        }
        else if(estado == Estado.EstadoC.ordinal()){
            return estadoFinal(lexema);
        }
        else if(estado == Estado.EstadoD.ordinal()){ // Estado A 2
            return estadoD(texto, posicao + 1, lexema, estado);
        }

        throw new RuntimeException("Error de execução");
    }

    private String mesmoEstado(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;
        return lexema;
    }

    private String estadoAnterior(String texto, int posicao, String lexema, int estado){
        char c = texto.charAt(posicao);
        lexema += c;

        if(estado == Estado.EstadoC.ordinal()){ // Estado C
            estado--;
            return estadoB(texto, posicao + 1, lexema, estado);
        }

        throw new RuntimeException("Error de execução");
    }

    /** ---- Metodos Gerais ---- **/

    private void relatorErro(){
        throw new RuntimeException("Não é um comentario");
    }

}

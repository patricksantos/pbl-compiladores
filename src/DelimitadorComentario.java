public class DelimitadorComentario {
    /** ---- Retorno do Lexema Analisado ---- **/

    public String getLexema(String texto, int posicao) { // Estado Inicial
        String lexema = ""; // Lexema que irá ser retornado

        try{
            for (int i = posicao; i < texto.length(); i++) {
                char c = texto.charAt(i);
                if (c == '/') {
                    lexema = estadoInicial(texto, i, lexema);
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

    private String estadoInicial(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '/' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(texto.charAt(posicao - 1) == '*'){
            return estadoFinal(lexema);
        }


        throw new RuntimeException("Error de execução");
    }

    private String estadoA(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '*' && texto.charAt(posicao - 1) == '/' ){
            return estadoB(texto, posicao + 1, lexema);
        }
        else if( c == '/' && texto.charAt(posicao - 1) == '/' ){
            return estadoD(texto, posicao + 1, lexema);
        }
        else{
            relatorErro();
        }

        throw new RuntimeException("Error de execução");
    }

    private String estadoB(String texto, int posicao, String lexema){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == '*') {
                lexema += c;
                return estadoC(texto, i + 1, lexema);
            }
            else{
                lexema += c;;
            }
        }
        throw new RuntimeException("Error de execução");
    }

    private String estadoC(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '/' && texto.charAt(posicao - 1) == '*') {
            return estadoFinal(lexema);
        }
        else {
            return estadoB(texto, posicao + 1, lexema);
        }
    }

    private String estadoD(String texto, int posicao, String lexema){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == 'n' && texto.charAt(i - 1) == '\\') {
                lexema += c;
                return estadoFinal(lexema);
            }
            else{
                lexema += c;
            }
        }
        throw new RuntimeException("Error de execução");
    }

    private String estadoFinal(String lexema){
        return lexema;
    }

    /** ---- Metodos Gerais ---- **/

    private void relatorErro(){
        throw new RuntimeException("Não é um comentario");
    }

}

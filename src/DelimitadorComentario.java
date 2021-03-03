public class DelimitadorComentario {
    private String lexema = ""; // Lexema que irá ser retornado
    private int estado = 0; // Estado do Autômato

    /** ---- Retorno do Lexema Analisado ----
     *  Posição onde o analisador irá começar
     *  Texto do input                     **/
    public String getLexema(String texto, int posicao) { // Estado Inicial

        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if( c == '/' ){
                estadoInicial(texto, i);
                break;
            }
        }

        String retornoLexema = this.lexema;
        this.lexema = "";

        return retornoLexema;
    }

    /** ---- Estados ---- **/

    private void estadoInicial(String texto, int posicao){
        char c = texto.charAt(posicao);
        if( c == '/' ){
            this.estado = 0;
            proximoEstado(texto, posicao);
        }
        else if(texto.charAt(posicao - 1) == '*'){
            estadoFinal();
        }
    }

    private void estadoA(String texto, int posicao){
        char c = texto.charAt(posicao);

        if( c == '*' && texto.charAt(posicao - 1) == '/' ){
            proximoEstado(texto, posicao);
        }
        else if( c == '/' && texto.charAt(posicao - 1) == '/' ){
            this.estado = 5;
            proximoEstado(texto, posicao);
        }
    }

    private void estadoB(String texto, int posicao){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == '*') {
                proximoEstado(texto, i);
                break;
            }
            else{
                mesmoEstado(texto, i);
            }

        }
    }

    private void estadoC(String texto, int posicao){
        char c = texto.charAt(posicao);

        if( c == '/' && texto.charAt(posicao - 1) == '*') {
            proximoEstado(texto, posicao);
        }
        else {
            estadoAnterior(texto, posicao);
        }
    }

    private void estadoD(String texto, int posicao){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == 'n' && texto.charAt(i - 1) == '\\') {
                this.estado = 3;
                proximoEstado(texto, i);
                break;
            }
            else{
                mesmoEstado(texto, i);
            }

        }
    }

    private void estadoFinal(){
        this.estado = 0;
    }

    /** ---- Logica entre os Estados ---- **/

    private void proximoEstado(String texto, int posicao){
        char c = texto.charAt(posicao);
        this.lexema += c;

        if(getEstado() == 0){
            this.estado++;
            estadoA(texto, posicao + 1);
        }
        else if(getEstado() == 1){
            this.estado++;
            estadoB(texto, posicao + 1);
        }
        else if(getEstado() == 2){
            this.estado++;
            estadoC(texto, posicao + 1);
        }
        else if(getEstado() == 5){
            estadoD(texto, posicao + 1);
        }
        else if(getEstado() == 3){
            estadoFinal();
        }

    }

    private void mesmoEstado(String texto, int posicao){
        char c = texto.charAt(posicao);
        this.lexema += c;
    }

    private void estadoAnterior(String texto, int posicao){
        char c = texto.charAt(posicao);
        this.lexema += c;

        if(getEstado() == 3){
            this.estado--;
            estadoB(texto, posicao + 1);
        }
    }

    /** ---- Metodos Gerais ---- **/

    private void relatorErro(){
    }

    private int getEstado() {
        return estado;
    }
}

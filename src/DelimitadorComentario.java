public class DelimitadorComentario {
    private String lexema = ""; // Lexema que irá ser retornado
    private int estado = 0; // Estado do Autômato

    // Posição onde o analisador irá começar
    // Texto do input
    public String getLexema(String texto, int posicao) // Estado Inicial
    {
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if( c == '/' ){
                estadoInicial(texto, i);
                break;
            }
        }

        return this.lexema;
    }

    /** ---- Estados ---- **/

    private void estadoInicial(String texto, int posicao){
        char c = texto.charAt(posicao);

        if( c == '/' ){
            this.lexema += c;
            this.estado = 0;
            proximoEstado(texto, posicao);
        }
        else if(texto.charAt(posicao - 1) == '*'){
            estadoFinal();
        }
    }

    private void estadoA(String texto, int posicao){
        char c = texto.charAt(posicao);

        if( c == '*' && texto.charAt(posicao - 1) == '/'){
            this.lexema += c;
            this.estado++;
            proximoEstado(texto, posicao);
        }
    }

    private void estadoB(String texto, int posicao){
        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == '*') {
                this.lexema += c;
                this.estado++;
                proximoEstado(texto, i);
                break;
            }
            else{
                this.lexema += c;
            }

        }
    }

    private void estadoB2(String texto, int posicao){

    }

    private void estadoC(String texto, int posicao){
        char c = texto.charAt(posicao);

        if( c == '/' && texto.charAt(posicao - 1) == '*') {
            this.lexema += c;
            this.estado++;
            proximoEstado(texto, posicao);
        }
        else {
            this.lexema += c;
            this.estado--;
            estadoAnterior(texto, posicao);
        }
    }

    private void estadoFinal(){
        this.estado = 0;
    }

    /** ---- Logica entre os Estados ---- **/

    private void proximoEstado(String texto, int posicao){

        if(getEstado() == 0){
            estadoA(texto, posicao + 1);
        }
        else if(getEstado() == 1){
            estadoB(texto, posicao + 1);
        }
        else if(getEstado() == 2){
            estadoC(texto, posicao + 1);
        }
        else if(getEstado() == 3){
            estadoFinal();
        }
    }

    private void mesmoEstado(String texto, int posicao){

        if(getEstado() == 2){
            estadoB(texto, posicao + 1);
        }
    }

    private void estadoAnterior(String texto, int posicao){

        if(getEstado() == 3){
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

package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresLogicos;

public class OperadoresLogicosImpl implements IOperadoresLogicos {
    /** ---- Retorno do Lexema Analisado ---- **/

    private int posicaoFinal = 0;

    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = ""; // Lexema que irá ser retornado
        //try {
        //antes de mandar o texto para o automato dar um trim para tirar os espaços em brancos que vem antes e depois das frases.
        return estadoInicial(texto, posicao, lexema);
        /*}catch (Exception e){
            return e.getMessage();
        }*/
    }

    /** ---- Estados ---- **/

    private Token estadoInicial(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '&' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(c == '|'){
            return estadoB(texto, posicao + 1, lexema);
        }
        else if(c == '!'){
            return estadoC(texto,posicao + 1, lexema);
        }

        return null;
        //erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoA(String texto, int posicao, String lexema){
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '&') {
                lexema += c;

                return estadoFinal(posicao + 1, lexema);
            }
        }

        setPosicaoFinal(posicao - 1);
        return null;
        //Erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoB(String texto, int posicao, String lexema){
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);

            if (c == '|') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            }
        }

        setPosicaoFinal(posicao - 1);
        return null;
        //Erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoC(String texto, int posicao, String lexema){

        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);

            if (c == '=') {
                setPosicaoFinal(posicao - 1);
                return null;
            }
        }

        return estadoFinal(posicao + 1, lexema);
        //Erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("Operador lógico",lexema);
    }

    /** ---- Metodos Gerais ---- **/

    @Override
    public void getError() {

    }

    @Override
    public int getPosicaoFinal(){
        return this.posicaoFinal;
    }

    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }
}



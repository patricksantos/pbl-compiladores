package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresAritmeticos;

public class OperadoresAritmeticosImpl implements IOperadoresAritmeticos {

    private int posicaoFinal;

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

        if( c == '+' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(c == '-'){
            return estadoB(texto, posicao + 1, lexema);
        }
        else if(c == '*'){
            return estadoFinal(posicao + 1, lexema);
        }
        else if(c == '/'){
            return estadoFinal(posicao + 1, lexema);
        }

        return null;
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoA(String texto, int posicao, String lexema){
        //System.out.println("EstadoA");
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '+') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            } else {
                //retornar token do tipo +
                //setPosicaoFinal(posicao);
                //return new Token("Operador Aritmético", lexema);
                return estadoFinal(posicao, lexema);
            }
        }
        //retornar token do tipo +
        //setPosicaoFinal(posicao);
        //return new Token("Operador Aritmético", lexema);
        return estadoFinal(posicao, lexema);
    }

    private Token estadoB(String texto, int posicao, String lexema){
        //System.out.println("EstadoC");
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '-') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            } else {
                //retornar token do tipo -
                //setPosicaoFinal(posicao);
                //return new Token("Operador Aritmético", lexema);
                estadoFinal(posicao, lexema);
            }
        }
        //retornar token do tipo - o
        //setPosicaoFinal(posicao);
        //return new Token("Operador Aritmético", lexema);
        return estadoFinal(posicao, lexema);
    }

    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("ART", lexema, false);
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

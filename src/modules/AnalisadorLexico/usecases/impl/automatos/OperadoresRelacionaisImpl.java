package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAutomato;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresRelacionais;

public class OperadoresRelacionaisImpl implements IOperadoresRelacionais {
    private int posicaoFinal;

    @Override
    public Token getToken(String texto, int posicao) {

        String lexema = ""; // Lexema que irá ser retornado
        Token token;
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

        if( c == '>' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(c == '<'){
            return estadoB(texto, posicao + 1, lexema);
        }
        else if(c == '!'){
            return estadoC(texto, posicao + 1, lexema);
        }
        else if(c == '='){
            return estadoD(texto, posicao + 1, lexema);
        }

        return null;
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoA(String texto, int posicao, String lexema){

        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            } else {
                //retornar token do tipo >
                return estadoFinal(posicao, lexema);
            }
        }
        //retornar token do tipo >
        //setPosicaoFinal(posicao);
        return estadoFinal(posicao, lexema);
    }

    private Token estadoB(String texto, int posicao, String lexema){

        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            } else {
                //retornar token do tipo <
                return estadoFinal(posicao, lexema);
            }
        }
        //retornar token do tipo <
        //setPosicaoFinal(posicao);
        return estadoFinal(posicao, lexema);

    }

    private Token estadoC(String texto, int posicao, String lexema){

        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            } else {
                setPosicaoFinal(posicao-1);
                return null;
            }
        }
        setPosicaoFinal(posicao-1);
        return null;
    }

    private Token estadoD(String texto, int posicao, String lexema){

        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                return estadoFinal(posicao + 1, lexema);
            } else {
                //retornar token do tipo =
                return estadoFinal(posicao, lexema);
            }
        }
        //retornar token do tipo =
        //setPosicaoFinal(posicao);
        return estadoFinal(posicao, lexema);
    }

    private Token estadoFinal(int posicao,String lexema){
        setPosicaoFinal(posicao);
        return new Token("Operador relacional",lexema);
    }

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
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
        token = estadoInicial(texto, posicao, lexema);
        return token;
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
            return estadoC(texto, posicao + 1, lexema);
        }
        else if(c == '!'){
            return estadoF(texto, posicao + 1, lexema);
        }
        else if(c == '='){
            return estadoG(texto, posicao + 1, lexema);
        }

        return null;
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoA(String texto, int posicao, String lexema){
        return null;
    }

    private Token estadoB(String texto, int posicao, String lexema){
        return null;
    }

    private Token estadoC(String texto, int posicao, String lexema){
        return null;
    }

    private Token estadoD(String texto, int posicao, String lexema){
        return null;
    }

    private Token estadoE(String texto, int posicao, String lexema){
        return null;
    }

    private Token estadoF(String texto, int posicao, String lexema){
        return null;
    }

    private Token estadoG(String texto, int posicao, String lexema){
        return null;
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

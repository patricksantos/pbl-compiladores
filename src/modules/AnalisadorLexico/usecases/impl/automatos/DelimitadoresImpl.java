package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.IDelimitadores;

public class DelimitadoresImpl implements IDelimitadores {

    private int posicaoFinal;

    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = ""; // Lexema que irá ser retornado
        //try {
        //antes de mandar o texto para o automato dar um trim para tirar os espaços em brancos que vem antes e depois das frases.
        return estadoInicialFinal(texto, posicao, lexema);
        /*}catch (Exception e){
            return e.getMessage();
        }*/
    }

    /** ---- Estados ---- **/

    private Token estadoInicialFinal(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '.' || c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']' || c == ';' ){
            setPosicaoFinal(posicao + 1);
            return new Token("Delimitador", lexema);
        }

        return null;
        //throw new RuntimeException("Error de execução");
    }

    @Override
    public int getPosicaoFinal(){
        return this.posicaoFinal;
    }

    @Override
    public void getError() {

    }

    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

}

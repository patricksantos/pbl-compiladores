package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAutomato;
import modules.AnalisadorLexico.usecases.facades.automatos.ISimbolo;

public class SimbolosImpl implements ISimbolo {
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

    private Token estadoInicial(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        //Verifica se está nas posições aceita da tabela ASCII
        /*if((texto.charAt(posicao) >= ' ' && texto.charAt(posicao) <= '!') ||
                (texto.charAt(posicao) >= '#' && texto.charAt(posicao) <= '~') ) {*/
        if(texto.charAt(posicao) == '!' ||
                (texto.charAt(posicao) >= '#' && texto.charAt(posicao) <= '~') ) {
            return estadoFinal(posicao + 1, lexema);
        }

        return null;
        //erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("Símbolo",lexema);
    }

    @Override
    public void getError() {

    }

    @Override
    public int getPosicaoFinal() {
        return this.posicaoFinal;
    }

    public void setPosicaoFinal(int posicaoFinal) {

        this.posicaoFinal = posicaoFinal;
    }
}

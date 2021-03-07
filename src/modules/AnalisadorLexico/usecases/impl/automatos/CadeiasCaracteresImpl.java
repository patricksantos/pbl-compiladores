package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.ICadeiasCaracteres;

public class CadeiasCaracteresImpl implements ICadeiasCaracteres {

    private int posicaoFinal;
    private String referencialLetra = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ ";
    private String referencialDigito = "123456789";

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

        if( c == '"' ){
            return estadoA(texto, posicao + 1, lexema);
        }

        return null;
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoA(String texto, int posicao, String lexema){

        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            //int auxiliarLetra = referencialLetra.indexOf(c);
            //int auxiliarDigito = referencialDigito.indexOf(c);

            while (c != '"' ) {
                //Confere se está nas regras de formação de uma cadeia de caracteres
                if((texto.charAt(posicao) >= ' ' && texto.charAt(posicao) <= '!') || (texto.charAt(posicao) >= '#' && texto.charAt(posicao) <= '~') ) {
                    lexema += c;
                    posicao++;
                    c = texto.charAt(posicao);
                   // auxiliarLetra = referencialLetra.indexOf(c);
                   // auxiliarDigito = referencialDigito.indexOf(c);
                }else{
                    return null;
                }
            }
            lexema += c;
            return estadoFinal(posicao + 1, lexema);
        }
        return null;
    }

    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("Cadeia de caracteres", lexema);
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

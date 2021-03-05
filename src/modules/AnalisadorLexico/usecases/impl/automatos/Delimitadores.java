package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;

public class Delimitadores implements IAutomato {

    private IAnalisadorLexico analisador;

    public Delimitadores(IAnalisadorLexico analisador){
        this.analisador = analisador;
    }
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

    @Override
    public void getError() {

    }

    /** ---- Estados ---- **/

    private Token estadoInicial(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '.' || c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']' || c == ';' ){
            Token token = new Token("Delimitador", lexema);
            this.analisador.adicionarToken(token);
            this.analisador.setPosicao(posicao + 1);
            return token;
        }

        return null;
        //throw new RuntimeException("Error de execução");
    }
}

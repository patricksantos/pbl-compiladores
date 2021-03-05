package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;

public class OperadoresLogicosImpl implements IAutomato {
    /** ---- Retorno do Lexema Analisado ---- **/
    private IAnalisadorLexico analisador;

    public OperadoresLogicosImpl(IAnalisadorLexico analisador){
        this.analisador = analisador;
    }

    public String getLexema(String texto, int posicao) { // Estado Inicial
        String lexema = ""; // Lexema que irá ser retornado
        return "a";
        /*try{
            for (int i = posicao; i < texto.length(); i++) {
                char c = texto.charAt(i);
                if (c == '/') {
                    lexema = estadoInicial(texto, i, lexema);
                    break;
                }
                if(i == texto.length() - 1)
                    throw new RuntimeException("Não é um comentario");
            }

            return lexema;
        }catch (Exception e){
            return e.getMessage();
        }*/
        /*try {
            //antes de mandar o texto para o automato dar um trim para tirar os espaços em brancos que vem antes e depois das frases.
            lexema = estadoInicial(texto, posicao, lexema);
            return lexema;
        }catch (Exception e){
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
            return estadoC(texto, posicao + 1, lexema);
        }
        else if(c == '!'){
            return estadoE(texto, posicao + 1, lexema);
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
                return estadoB(texto, posicao + 1, lexema);
            }
        }
        this.analisador.setPosicao(posicao-1);
        return null;
        //Erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoB(String texto, int posicao, String lexema){
      //adiciona o token operador lógico na lista de token do tipo &&
        Token token = new Token("Operador lógico",lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private Token estadoC(String texto, int posicao, String lexema){
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);

            if (c == '|') {
                lexema += c;
                return estadoD(texto, posicao + 1, lexema);
            }
        }
        this.analisador.setPosicao(posicao-1);
        return null;
        //Erro na identificação do token
        //throw new RuntimeException("Error de execução");
    }

    private Token estadoD(String texto, int posicao, String lexema){
        //adiciona o token operador lógico na lista de token do tipo ||
        Token token = new Token("Operador lógico",lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private Token estadoE(String texto, int posicao, String lexema){
        //adiciona o token operador lógico na lista de token do tipo !
        Token token = new Token("Operador lógico",lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    /** ---- Metodos Gerais ---- **/

    private void relatorErro(){
        throw new RuntimeException("Não é operador lógico");
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
}



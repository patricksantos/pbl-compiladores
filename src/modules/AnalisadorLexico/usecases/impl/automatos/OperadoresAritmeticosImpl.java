package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;

public class OperadoresAritmeticosImpl implements IAutomato {

    private IAnalisadorLexico analisador;
    public OperadoresAritmeticosImpl(IAnalisadorLexico analisador){
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
    }

    /** ---- Estados ---- **/

    private Token estadoInicial(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '+' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(c == '-'){
            return estadoC(texto, posicao + 1, lexema);
        }
        else if(c == '*'){
            return estadoE(texto, posicao + 1, lexema);
        }
        else if(c == '/'){
            return estadoF(texto, posicao + 1, lexema);
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
                return estadoB(texto, posicao + 1, lexema);
            } else {
                //retornar token do tipo + e voltar uma posição
                Token token = new Token("Operador aritmético", lexema);
                this.analisador.adicionarToken(token);
                this.analisador.setPosicao(posicao);
                return token;
            }
        }
        //retornar token do tipo + e voltar uma posição
        Token token = new Token("Operador aritmético", lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;

    }

    private Token estadoB(String texto, int posicao, String lexema){
        //System.out.println("EstadoB");
        //adiciona o token operador lógico na lista de token do tipo ++
        Token token = new Token("Operador aritmético", lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private Token estadoC(String texto, int posicao, String lexema){
        //System.out.println("EstadoC");
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '-') {
                lexema += c;
                return estadoD(texto, posicao + 1, lexema);
            } else {
                //retornar token do tipo - e voltar uma posição
                Token token = new Token("Operador aritmético", lexema);
                this.analisador.adicionarToken(token);
                this.analisador.setPosicao(posicao);
                return token;
            }
        }
        //retornar token do tipo - e voltar uma posição
        Token token = new Token("Operador aritmético", lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private Token estadoD(String texto, int posicao, String lexema){
        //System.out.println("EstadoD" + lexema);
        //adiciona o token operador lógico na lista de token do tipo --
        Token token = new Token("Operador aritmético", lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private Token estadoE(String texto, int posicao, String lexema){
        //System.out.println("EstadoE");
        //adiciona o token operador lógico na lista de token do tipo *
        Token token = new Token("Operador aritmético",lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private Token estadoF(String texto, int posicao, String lexema){
        //System.out.println("EstadoF");
        //adiciona o token operador lógico na lista de token do tipo /
        Token token = new Token("Operador aritmético", lexema);
        this.analisador.adicionarToken(token);
        this.analisador.setPosicao(posicao);
        return token;
    }

    private String estadoFinal(String lexema){
        return lexema;
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

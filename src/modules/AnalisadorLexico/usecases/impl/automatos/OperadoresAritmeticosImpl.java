package modules.AnalisadorLexico.usecases.impl.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;

public class OperadoresAritmeticosImpl implements IAutomato {
    /** ---- Retorno do Lexema Analisado ---- **/
    private IAnalisadorLexico analisador;
    public OperadoresAritmeticosImpl(IAnalisadorLexico analisador){
        this.analisador = analisador;
    }

    public String getLexema(String texto, int posicao) { // Estado Inicial
        String lexema = ""; // Lexema que irá ser retornado

        try{
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
        }
    }

    /** ---- Estados ---- **/

    private String estadoInicial(String texto, int posicao, String lexema){
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


        throw new RuntimeException("Error de execução");
    }

    private String estadoA(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '+'){
            return estadoB(texto, posicao + 1, lexema);
        }
        else{
            //retornar token do tipo + e voltar uma posição
            return lexema;
        }

    }

    private String estadoB(String texto, int posicao, String lexema){
        //adiciona o token operador lógico na lista de token do tipo ++
        return lexema;
    }

    private String estadoC(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '-') {
            return estadoD(texto, posicao + 1, lexema);
        }
        else{
            //retornar token do tipo - e voltar uma posição
            return lexema;
        }

    }

    private String estadoD(String texto, int posicao, String lexema){
        //adiciona o token operador lógico na lista de token do tipo --
        return lexema;
    }

    private String estadoE(String texto, int posicao, String lexema){
        //adiciona o token operador lógico na lista de token do tipo *
        return lexema;
    }

    private String estadoF(String texto, int posicao, String lexema){
        //adiciona o token operador lógico na lista de token do tipo /
        return lexema;
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
        return null;
    }

    @Override
    public void getError() {

    }
}

package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IDelimitadorComentario;

import java.util.ArrayList;

public class DelimitadorComentarioImpl implements IDelimitadorComentario {
    private int posicaoFinal;
    private int linhaFinal;

    /** ---- Retorno do Lexema Analisado ---- **/

    @Override
    public Token getToken(ArrayList<String> arquivo, String texto, int linha, int posicao) {
        try{
            String lexema = ""; // Lexema que irá ser retornado
            String result = estadoInicial(arquivo, texto, linha, posicao, lexema);

            if(result == null) {
                return null;
            }
            else {
                return new Token("Comentario", result);
            }
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Token getToken(String texto, int posicao) { // Não irá ser utilizado
        return null;
    }

    /** ---- Estados ---- **/

    private String estadoInicial(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '/' ){
            return estadoA(arquivo, texto, linha, posicao + 1, lexema);
        }

        //throw new RuntimeException("Error de execução");

        return null;
    }

    private String estadoA(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '*' && texto.charAt(posicao - 1) == '/' )
        {
            if( posicao + 1 >= texto.length() )
            {
                linha += 1;
                texto = arquivo.get(linha);

                return estadoB(arquivo, texto, linha, 0, lexema);
            }
            else{
                return estadoB(arquivo, texto, linha, posicao + 1, lexema);
            }
        }
        else if( c == '/' && texto.charAt(posicao - 1) == '/' ){
            return estadoD(linha, texto, posicao + 1, lexema);
        }
        // getError();
        return null;
    }

    private String estadoB(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        StringBuilder lexemaBuilder = new StringBuilder(lexema);

        if( posicao + 1 >= texto.length()){
            linha = linha + 1;
            texto = arquivo.get(linha);

            return estadoB(arquivo, texto, linha, 0, lexemaBuilder.toString());
        }

        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == '*') {
                lexemaBuilder.append(c);
                return estadoC(arquivo, texto, linha, i + 1, lexemaBuilder.toString());
            }
            else{
                lexemaBuilder.append(c);
                if( i + 1 >= texto.length()){
                    linha = linha + 1;
                    texto = arquivo.get(linha);

                    return estadoB(arquivo, texto, linha, 0, lexemaBuilder.toString());
                }
            }
        }
        // throw new RuntimeException("Error de execução");

        return null;
    }

    private String estadoC(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '/' && texto.charAt(posicao - 1) == '*') {
            return estadoFinal(linha, posicao + 1, lexema);
        }
        else {
            return estadoB(arquivo, texto, linha, posicao + 1, lexema);
        }
    }

    private String estadoD(int linha, String texto, int posicao, String lexema){
        StringBuilder lexemaBuilder = new StringBuilder(lexema);

        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            lexemaBuilder.append(c);
        }
        lexema = lexemaBuilder.toString();

        return estadoFinal(linha, posicao + 1, lexema);
        // throw new RuntimeException("Error de execução");
    }

    private String estadoFinal(int linha, int posicao, String lexema){
        setPosicaoFinal(posicao);
        setLinhaFinal(linha);
        return lexema;
    }

    /** ---- Metodos Gerais ---- **/

    @Override
    public void getError(){
        throw new RuntimeException("Não é um comentario");
    }

    @Override
    public int getPosicaoFinal(){
        return this.posicaoFinal;
    }

    private void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

    @Override
    public int getLinhaFinal() {
        return this.linhaFinal;
    }

    private void setLinhaFinal(int linhaFinal) {
        this.linhaFinal = linhaFinal;
    }
}

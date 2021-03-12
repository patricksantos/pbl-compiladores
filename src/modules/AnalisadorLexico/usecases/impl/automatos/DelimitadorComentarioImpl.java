package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IDelimitadorComentario;

import java.util.ArrayList;

public class DelimitadorComentarioImpl implements IDelimitadorComentario {
    private int posicaoFinal;
    private int linhaFinal;
    private boolean error = false;

    /** ---- Retorno do Lexema Analisado ---- **/

    @Override
    public Token getToken(ArrayList<String> arquivo, String texto, int linha, int posicao) {
        try{
            this.error = false;
            String lexema = ""; // Lexema que irá ser retornado
            String result = estadoInicial(arquivo, texto, linha, posicao, lexema);
            System.out.println(result);
            if(this.error){
                return new Token("CoMF", result, true);
            }
            else if(result == null) {
                return null;
            }
            else {
                return new Token("Comentario", result, false);
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

            if( linha + 1 >= arquivo.size() ) {
                setPosicaoFinal(posicao);
                setLinhaFinal(arquivo.size() + 1);
                this.error = true;

                return lexemaBuilder.toString();
            }

            linha = linha + 1;
            texto = arquivo.get(linha);

            while (texto.isEmpty()){
                linha = linha + 1;
                texto = arquivo.get(linha);
            }

            return estadoB(arquivo, texto, linha, 0, lexemaBuilder.toString());
        }

        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);
            if(c == '*') {

                lexemaBuilder.append(c);
                return estadoC(arquivo, texto, linha, i + 1, lexemaBuilder.toString());
            }

            lexemaBuilder.append(c);
        }
        // throw new RuntimeException("Error de execução");
        return estadoB(arquivo, texto, linha, texto.length(), lexemaBuilder.toString());
    }

    private String estadoB1(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        StringBuilder lexemaBuilder = new StringBuilder(lexema);

        if( posicao + 1 == texto.length()){
            if( linha + 1 >= arquivo.size() )
            {
                setPosicaoFinal(posicao);
                setLinhaFinal(arquivo.size() + 1);
                this.error = true;

                return lexemaBuilder.toString();
            }
            char c = texto.charAt(posicao);
            lexemaBuilder.append(c);

            linha = linha + 1;
            texto = arquivo.get(linha);

            while (texto.isEmpty()){
                linha = linha + 1;
                texto = arquivo.get(linha);
            }

            return estadoB(arquivo, texto, linha, 0, lexemaBuilder.toString());
        }

        for (int i = posicao; i < texto.length(); i++)
        {
            char c = texto.charAt(i);

            if(c == '*') {
                lexemaBuilder.append(c);
                return estadoC(arquivo, texto, linha, i + 1, lexemaBuilder.toString());
            }
            else {
                lexemaBuilder.append(c);

                if( i + 1 >= texto.length()){
                    linha = linha + 1;

                    if( linha + 1 >= arquivo.size() )
                    {
                        setPosicaoFinal(posicao);
                        setLinhaFinal(arquivo.size() + 1);
                        this.error = true;

                        return lexemaBuilder.toString();
                    }

                    texto = arquivo.get(linha);
                    while (texto.isEmpty()){
                        linha = linha + 1;
                        texto = arquivo.get(linha);
                    }

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

        if( c == '/' ) {

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
            if( i + 1 == texto.length() ){
                lexema = lexemaBuilder.toString();
                return estadoFinal(linha, i + 1, lexema);
            }
        }

        return null;
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

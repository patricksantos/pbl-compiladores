package modules.AnalisadorLexico.usecases.impl;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.IDelimitadorComentario;
import modules.AnalisadorLexico.usecases.facades.automatos.IDelimitadores;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresAritmeticos;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresLogicos;

import java.util.ArrayList;

public class AnalisadorLexicoImpl implements IAnalisadorLexico {

    private final IDelimitadorComentario delimitadorComentario;
    private final IOperadoresLogicos operadoresLogicos;
    private final IOperadoresAritmeticos operadoresAritmeticos;
    private final IDelimitadores delimitadores;

    private int linhaAtual; // Guarda a linha atual que o analisador está lendo
    private int posicao; // Guarda a posição atual na linha que o automato está lendo
    private int quantidadeLinhas; // Quantidade de linhas que o arquivo tem

    private ArrayList<String> conteudoArquivo; // As linhas que foram lidas no arquivo de entrada
    private ArrayList<Token> tokens; // Guarda os tokens identficados no arquivo

    public AnalisadorLexicoImpl(IDelimitadorComentario delimitadorComentario, IOperadoresLogicos operadoresLogicos, IOperadoresAritmeticos operadoresAritmeticos, IDelimitadores delimitadores) {
        this.delimitadorComentario = delimitadorComentario;
        this.linhaAtual = 0;
        this.posicao = 0;
        this.operadoresLogicos = operadoresLogicos;
        this.operadoresAritmeticos = operadoresAritmeticos;
        this.delimitadores = delimitadores;
    }

    @Override
    public Token analise(String texto, int posicao) {

        ArrayList<Token> tokens = new ArrayList<>();

        for(String linha: this.conteudoArquivo){
            Token token = delimitadorComentario.getToken(linha, this.posicao);
            tokens.add(token);
        }

        return delimitadorComentario.getToken(texto, posicao);
    }

    @Override
    public ArrayList<Token> analiseArquivo(ArrayList<String> conteudoArquivo) {
        setLinhaAtual(0);
        setPosicao(0);

        this.conteudoArquivo = conteudoArquivo;
        this.tokens = new ArrayList<>();

        for(this.linhaAtual = 0; this.linhaAtual < this.conteudoArquivo.size(); this.linhaAtual++)
        {
            String linha = this.conteudoArquivo.get(this.linhaAtual);

            while( this.posicao < linha.length() )
            {
                Token resultadoDelimitadorComentario = delimitadorComentario.getToken(this.conteudoArquivo, linha, this.linhaAtual, this.posicao);

                if( resultadoDelimitadorComentario == null )
                {
                    operadoresLogicos(linha);
                }
                else{
                    setLinhaAtual(delimitadorComentario.getLinhaFinal());
                    setPosicao(delimitadorComentario.getPosicaoFinal());
                }
            }

            this.posicao = 0;
        }

        return this.tokens;
    }

    private void operadoresLogicos(String linha){
        Token resultadoOperadoresLogicos = operadoresLogicos.getToken(linha, this.posicao);

        if(resultadoOperadoresLogicos == null)
        {
            operadoresAritmeticos(linha);
        }
        else{
            setPosicao(operadoresLogicos.getPosicaoFinal());
            this.tokens.add(resultadoOperadoresLogicos);
        }
    }

    private void operadoresAritmeticos(String linha){
        Token resultadoOperadoresAritmeticos = operadoresAritmeticos.getToken(linha, this.posicao);

        if(resultadoOperadoresAritmeticos == null)
        {
            delimitadores(linha);
        }
        else{
            setPosicao(operadoresAritmeticos.getPosicaoFinal());
            this.tokens.add(resultadoOperadoresAritmeticos);
        }
    }

    private void delimitadores(String linha){
        Token resultadoDelimitadores = delimitadores.getToken(linha, this.posicao);

        if(resultadoDelimitadores == null) {
            setPosicao(posicao + 1);
        }
        else{
            setPosicao(delimitadores.getPosicaoFinal());
            this.tokens.add(resultadoDelimitadores);
        }
    }

    @Override
    public String proximaLinha(){
        return conteudoArquivo.get(linhaAtual + 1);
    }

    @Override
    public void adicionarToken(Token token){
        this.tokens.add(token);
    }

    @Override
    public void setPosicao(int posicao){
        this.posicao = posicao;
    }

    public void setLinhaAtual(int linhaAtual) {
        this.linhaAtual = linhaAtual;
    }
}

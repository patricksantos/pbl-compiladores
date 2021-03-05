package modules.AnalisadorLexico.usecases.impl;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;
import modules.AnalisadorLexico.usecases.impl.automatos.OperadoresAritmeticosImpl;
import modules.AnalisadorLexico.usecases.impl.automatos.OperadoresLogicosImpl;

import java.util.ArrayList;

public class AnalisadorLexicoImpl implements IAnalisadorLexico {

    IAutomato delimitadorComentario;
    private IAutomato operadorLogico;
    private IAutomato operadorAritmetico;
    private int linhaAtual; // Guarda a linha atual que o analisador está lendo
    private int posicao; // Guarda a posição atual na linha que o automato está lendo
    private int quantidadeLinhas; // Quantidade de linhas que o arquivo tem
    private ArrayList<String> conteudoArquivo; // As linhas que foram lidas no arquivo de entrada
    private ArrayList<Token> tokens; // Guarda os tokens identficados no arquivo

    public AnalisadorLexicoImpl(IAutomato delimitadorComentario) {
        this.delimitadorComentario = delimitadorComentario;
        this.linhaAtual = 0;
        this.posicao = 0;
        //this.conteudoArquivo = conteudoArquivo;
        //this.quantidadeLinhas = conteudoArquivo.size();
        this.operadorLogico = new OperadoresLogicosImpl(this);
        this.operadorAritmetico = new OperadoresAritmeticosImpl(this);
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
        this.conteudoArquivo = conteudoArquivo;
        this.quantidadeLinhas = conteudoArquivo.size();
        this.tokens = new ArrayList<>();
        for(String linha: this.conteudoArquivo){

            while(this.posicao < linha.length()) {

                if(operadorLogico.getToken(linha, this.posicao) == null){
                    if(operadorAritmetico.getToken(linha,this.posicao) == null){
                        this.posicao++;
                    }
                }


            }
            this.posicao = 0;
        }
        return this.tokens;
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
}

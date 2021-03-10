package modules.AnalisadorLexico.usecases.impl;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.*;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;

import java.util.ArrayList;

public class AnalisadorLexicoImpl implements IAnalisadorLexico {

    private final IDelimitadorComentario delimitadorComentario;
    private final IOperadoresLogicos operadoresLogicos;
    private final IOperadoresAritmeticos operadoresAritmeticos;
    private final IDelimitadores delimitadores;
    private final IOperadoresRelacionais operadoresRelacionais;
    private final IPalavrasReservadasIdentificadores palavrasReservadasIdentificadores;
    private final ICadeiasCaracteres cadeiasCaracteres;
    private final ISimbolo simbolos;
    private final INumero numeros;

    private final ITabelaSimbolos tabelaSimbolos;

    private int linhaAtual; // Guarda a linha atual que o analisador está lendo
    private int posicao; // Guarda a posição atual na linha que o automato está lendo
    private int quantidadeLinhas; // Quantidade de linhas que o arquivo tem

    private ArrayList<String> conteudoArquivo; // As linhas que foram lidas no arquivo de entrada
    private ArrayList<Token> tokens; // Guarda os tokens identficados no arquivo
    private ArrayList<Token> erros; //Guarda os tokens que representam erros

    public AnalisadorLexicoImpl(ITabelaSimbolos tabelaSimbolos ,IDelimitadorComentario delimitadorComentario, IOperadoresLogicos operadoresLogicos,
                                IOperadoresAritmeticos operadoresAritmeticos, IDelimitadores delimitadores, IPalavrasReservadasIdentificadores palavrasReservadasIdentificadores,
                                IOperadoresRelacionais operadoresRelacionais, ICadeiasCaracteres cadeiasCaracteres, ISimbolo simbolos, INumero numeros) {
        this.delimitadorComentario = delimitadorComentario;
        this.linhaAtual = 0;
        this.posicao = 0;
        this.operadoresLogicos = operadoresLogicos;
        this.operadoresAritmeticos = operadoresAritmeticos;
        this.delimitadores = delimitadores;
        this.palavrasReservadasIdentificadores = palavrasReservadasIdentificadores;
        this.operadoresRelacionais = operadoresRelacionais;
        this.cadeiasCaracteres = cadeiasCaracteres;
        this.tabelaSimbolos = tabelaSimbolos;
        this.simbolos = simbolos;
        this.numeros = numeros;
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
        this.erros = new ArrayList<>();

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
                /*if(linha.charAt(posicao) >= '$' && linha.charAt(posicao) <= '~'){
                    System.out.println("opa");
                }
                posicao++;*/
            }

            this.posicao = 0;
        }

        this.tabelaSimbolos.getTokensTabela();

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
            resultadoOperadoresLogicos.setLinha(this.linhaAtual);
            if(resultadoOperadoresLogicos.getError()){
                this.erros.add(resultadoOperadoresLogicos);
            }else {
                this.tokens.add(resultadoOperadoresLogicos);
            }
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
            resultadoOperadoresAritmeticos.setLinha(this.linhaAtual);
            if(resultadoOperadoresAritmeticos.getError()){
                this.erros.add(resultadoOperadoresAritmeticos);
            }else{
                this.tokens.add(resultadoOperadoresAritmeticos);
            }
        }
    }

    private void delimitadores(String linha){
        Token resultadoDelimitadores = delimitadores.getToken(linha, this.posicao);

        if(resultadoDelimitadores == null) {
            operadoresRelacionais(linha);
        }
        else{
            setPosicao(delimitadores.getPosicaoFinal());
            resultadoDelimitadores.setLinha(this.linhaAtual);
            if(resultadoDelimitadores.getError()){
                this.erros.add(resultadoDelimitadores);
            }else{
                this.tokens.add(resultadoDelimitadores);
            }
        }
    }

    private void operadoresRelacionais(String linha){

        Token resultadoOperadoresRelacionais = this.operadoresRelacionais.getToken(linha, this.posicao);

        if(resultadoOperadoresRelacionais == null) {
            cadeiasCaracteres(linha);
        }
        else{
            setPosicao(operadoresRelacionais.getPosicaoFinal());
            resultadoOperadoresRelacionais.setLinha(this.linhaAtual);
            if(resultadoOperadoresRelacionais.getError()){
                this.erros.add(resultadoOperadoresRelacionais);
            }else{
                this.tokens.add(resultadoOperadoresRelacionais);
            }
        }
    }

    private void cadeiasCaracteres(String linha){

        Token resultadoCadeiasCaracteres = this.cadeiasCaracteres.getToken(linha, this.posicao);

        if(resultadoCadeiasCaracteres == null) {
            palavrasReservadasIdentificadores(linha);
        }
        else{
            setPosicao(cadeiasCaracteres.getPosicaoFinal());
            resultadoCadeiasCaracteres.setLinha(this.linhaAtual);
            if(resultadoCadeiasCaracteres.getError()){
                this.erros.add(resultadoCadeiasCaracteres);
            }else{
                this.tokens.add(resultadoCadeiasCaracteres);
            }
        }
    }

    private void palavrasReservadasIdentificadores(String linha){
        Token resultadoPalavrasReservadasIdentificadores = palavrasReservadasIdentificadores.getToken(linha, this.posicao);
        if(resultadoPalavrasReservadasIdentificadores == null) {
            //simbolos(linha);
            numeros(linha);
            //setPosicao(posicao + 1);
        }
        else{
            setPosicao(palavrasReservadasIdentificadores.getPosicaoFinal());
            resultadoPalavrasReservadasIdentificadores.setLinha(this.linhaAtual);
            if(resultadoPalavrasReservadasIdentificadores.getError()){
                this.erros.add(resultadoPalavrasReservadasIdentificadores);
            }else{
                this.tokens.add(resultadoPalavrasReservadasIdentificadores);
                this.tabelaSimbolos.setSimbolo(resultadoPalavrasReservadasIdentificadores.getLexema(), resultadoPalavrasReservadasIdentificadores.getTipo());
            }
        }
    }

    private void numeros(String linha){

        Token resultadoNumeros = this.numeros.getToken(linha, this.posicao);

        if(resultadoNumeros == null) {
            simbolos(linha);
        }
        else{
            setPosicao(numeros.getPosicaoFinal());
            resultadoNumeros.setLinha(this.linhaAtual);
            if(resultadoNumeros.getError()){
                this.erros.add(resultadoNumeros);
            }else{
                this.tokens.add(resultadoNumeros);
            }
        }
    }

    private void simbolos(String linha){

        Token resultadoSimbolos = this.simbolos.getToken(linha, this.posicao);

        if(resultadoSimbolos == null) {
            setPosicao(posicao + 1);
        }
        else{
            setPosicao(simbolos.getPosicaoFinal());
            resultadoSimbolos.setLinha(this.linhaAtual);
            this.erros.add(resultadoSimbolos);
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

    @Override
    public ArrayList<Token> getErros(){
        return this.erros;
    }
}

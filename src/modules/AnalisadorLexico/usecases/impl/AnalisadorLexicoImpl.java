package modules.AnalisadorLexico.usecases.impl;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.*;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;

import java.util.ArrayList;
/**
 * Classe responsável por controlar o fluxo dos automatos, e disponibilizar o conteudo que será lido por eles.
 * */

public class AnalisadorLexicoImpl implements IAnalisadorLexico {
    //Automatos
    private final IDelimitadorComentario delimitadorComentario; //Automato que verifica se é um comentário
    private final IOperadoresLogicos operadoresLogicos; //Automato de operadores lógicos
    private final IOperadoresAritmeticos operadoresAritmeticos; //Automato de operadores aritméticos
    private final IDelimitadores delimitadores; //Automato de delimitadores
    private final IOperadoresRelacionais operadoresRelacionais; //Automato de operadores relacionais
    private final IPalavrasReservadasIdentificadores palavrasReservadasIdentificadores; //Automato de identificadores e palavras reservadas
    private final ICadeiasCaracteres cadeiasCaracteres; //Automato de cadeias de caracteres
    private final ISimbolo simbolos; //Automato de símbolos
    private final INumero numeros; //Automato de números

    private final ITabelaSimbolos tabelaSimbolos;
    //Variaveis de controle do cursor de leitura do arquivo
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

    /**
     * Método que irá analisar o conteudo do arquivo, e irá gerar os tokens identificados
     * @param conteudoArquivo uma lista contendo as linhas que foram retiradas do arquivo.
     *
     * @return retorna a lista de tokens encontrados no arquivo que foi lido.
     * */
    @Override
    public ArrayList<Token> analiseArquivo(ArrayList<String> conteudoArquivo) {
        //Seta as informações padrões para o inicio da análise do arquivo.
        this.setLinhaAtual(0);
        this.setPosicao(0);
        this.conteudoArquivo = conteudoArquivo;
        this.tokens = new ArrayList<>();
        this.erros = new ArrayList<>();

        //Passar pelas linhas do arquivo
        for(this.linhaAtual = 0; this.linhaAtual < this.conteudoArquivo.size(); this.linhaAtual++)
        {

            //Passa pelos caracteres da linha
            while( this.posicao < this.conteudoArquivo.get(this.linhaAtual).length() )
            {
                //Pega uma linha para leitura
                String linha = this.conteudoArquivo.get(this.linhaAtual);

                Token resultadoDelimitadorComentario = delimitadorComentario.getToken(this.conteudoArquivo, linha, this.linhaAtual, this.posicao);
                //Caso não encontre nenhum comentário, outro automato é chamado.
                if( resultadoDelimitadorComentario == null )
                {
                    operadoresLogicos(linha);
                }
                else{
                    /*Caso encontre, nenhum token é formado, mas a linha e a posição são atualizados, para ignorar a
                    ocorrência de comentários*/
                    int l = this.linhaAtual;
                    setLinhaAtual(delimitadorComentario.getLinhaFinal());
                    setPosicao(delimitadorComentario.getPosicaoFinal());
                    //Caso exista algum comentário mal formado, um erro é adicionado a lista de erros.
                    if(resultadoDelimitadorComentario.getError()){
                        resultadoDelimitadorComentario.setLinha(l);
                        this.erros.add(resultadoDelimitadorComentario);
                        break;
                    }
                }
                /*if(linha.charAt(posicao) >= '$' && linha.charAt(posicao) <= '~'){
                    System.out.println("opa");
                }
                posicao++;*/
                System.out.println("OI");
            }

            this.posicao = 0;
        }

        this.tabelaSimbolos.getTokensTabela();

        return this.tokens;
    }

    /**
     * Método responsável por chamar o automáto que identifica operadores lógicos, e caso encontre um token desse tipo,
     * o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
    private void operadoresLogicos(String linha){
        //Chama o automato de operadores lógicos
        Token resultadoOperadoresLogicos = operadoresLogicos.getToken(linha, this.posicao);

        /*Caso o automato retone null, significa que não foi identificado nenhum token, e é chamado o proximo automato
        para descobrir se faz parte dele*/
        if(resultadoOperadoresLogicos == null)
        {
            operadoresAritmeticos(linha);
        }
        /*Caso encontre, é adicionado na lista de token se um token foi identificado, ou na lista de erros, se foi
        encontrado algum erro*/
        else{
            /* Atualiza a posição na linha contida no analisador lexico para a ultima posição que foi analisada pelo
            automato de operadores lógicos.*/
            setPosicao(operadoresLogicos.getPosicaoFinal());
            //Adiciona no token o número da linha que o mesmo estava no arquivo de entrada.
            resultadoOperadoresLogicos.setLinha(this.linhaAtual);

            if(resultadoOperadoresLogicos.getError()){
                this.erros.add(resultadoOperadoresLogicos);
            }else {
                this.tokens.add(resultadoOperadoresLogicos);
            }
        }
    }

    /**
     * Método responsável por chamar o automáto que identifica operadores aritmeticos, e caso encontre um token desse
     * tipo, o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
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

    /**
     * Método responsável por chamar o automáto que identifica delimitadores, e caso encontre um token desse tipo,
     * o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
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

    /**
     * Método responsável por chamar o automáto que identifica operadores relacionais, e caso encontre um token desse
     * tipo, o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
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

    /**
     * Método responsável por chamar o automáto que identifica cadeias de caracteres, e caso encontre um token desse
     * tipo, o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
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

    /**
     * Método responsável por chamar o automáto que identifica palavras reservadas e identificadores, e caso encontre
     * um token desse tipo, o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
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

    /**
     * Método responsável por chamar o automáto que identifica números, e caso encontre
     * um token desse tipo, o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
    private void numeros(String linha){

        Token resultadoNumeros = this.numeros.getToken(linha, this.posicao);

        if(resultadoNumeros == null) {
            simbolos(linha);
            //setPosicao(posicao + 1);
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

    /**
     * Método responsável por chamar o automáto que identifica símbolos, e caso encontre um token desse tipo,
     * o adiciona na lista de erros, pois os simbolos só podem estar dentro de cadeias de caracteres.
     * @param linha linha que está sendo analisada atualmente.
     * */
    private void simbolos(String linha){

        Token resultadoSimbolos = this.simbolos.getToken(linha, this.posicao);

        if(resultadoSimbolos == null) {

            if (this.posicao < linha.length()){
                char auxiliar = linha.charAt(posicao);
                if(auxiliar != ' ') {
                    if(auxiliar != '\t') {
                        Token tokenErro = new Token("CNPA", auxiliar + "", true);
                        tokenErro.setLinha(this.linhaAtual);
                        this.erros.add(tokenErro);
                    }
                }
            }
            setPosicao(posicao + 1);
        }
        else{
            setPosicao(simbolos.getPosicaoFinal());
            resultadoSimbolos.setLinha(this.linhaAtual);
            this.erros.add(resultadoSimbolos);
        }
    }

    /**
     * @return a proxima linha na lista de linhas do arquivo(conteudoArquivo)
     * */
    @Override
    public String proximaLinha(){
        return conteudoArquivo.get(linhaAtual + 1);
    }

    /**
     * Adiciona um token na lista de token
     * @param token token que será adicionado na lista de tokens
     * */
    @Override
    public void adicionarToken(Token token){
        this.tokens.add(token);
    }

    /**
     * Atualiza a posicao que está sendo analisada atualmente
     * @param posicao a posição atualizada
     * */
    @Override
    public void setPosicao(int posicao){
        this.posicao = posicao;
    }

    /**
     * atualiza o número da linha que está sendo analisada atualmente
     * @param linhaAtual o número da linha
     * */
    public void setLinhaAtual(int linhaAtual) {
        this.linhaAtual = linhaAtual;
    }

    /**
     * Retorna os erros enconrtrados no arquivo
     * @return uma lista de erros
     * */
    @Override
    public ArrayList<Token> getErros(){
        return this.erros;
    }
}

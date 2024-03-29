package modules.AnalisadorLexico.usecases.impl;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.*;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;

import java.util.ArrayList;
/**
 * Classe responsável por controlar o fluxo dos automatos, e disponibilizar o conteudo que será lido por eles.
 * O caracterserá testado pelo primeiro automato, caso nenhum token for detectado, o proximo automato será chamado e
 * assim por diante. Caso ele passe por todos os automátos e nenhum automato for encontrado, será considerado como um
 * caractere que não pertence a linguagem.
 * * */

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
    private int posicao; // Guarda a posição atual na linha que o analisador léxico está lendo
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

            while( this.posicao < this.conteudoArquivo.get(this.linhaAtual).length() )
            {
                //Pega a linha que está sendo linda atualmente
                String linha = this.conteudoArquivo.get(this.linhaAtual);

                //Manda a linha e a posição na linha que irá começar a leitura, para analisar caractere por caractere.
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
                    if(resultadoDelimitadorComentario.isError()){
                        resultadoDelimitadorComentario.setLinha(l);
                        this.erros.add(resultadoDelimitadorComentario);
                        break;
                    }
                }

            }
            //Como outra linha será lida, a posição é resetada, para que a leitura começãr do inicio.
            this.posicao = 0;
        }

        this.tabelaSimbolos.getTokensTabela();

        return this.tokens;
    }

    /************************************************* Automatos ******************************************************\

    /**
     * Método responsável por chamar o automáto que identifica operadores lógicos, e caso encontre um token desse tipo,
     * o adiciona na lista de token.
     * @param linha linha que está sendo analisada atualmente.
     * */
    private void operadoresLogicos(String linha){
        //Chama o automato de operadores lógicos, mandando a linha e a posição na linha que está sendo analisada.
        Token resultadoOperadoresLogicos = operadoresLogicos.getToken(linha, this.posicao);

        //Caso o automato retone null, significa que não foi identificado nenhum token, e é chamado o proximo automato

        if(resultadoOperadoresLogicos == null)
        {
            operadoresAritmeticos(linha);
        }
        /*Caso encontre um token, é adicionado na lista de tokens, ou na lista de erros, se o
        token encontrado representa um token de erro*/
        else{
            /*Atualiza a posição na linha contida no analisador lexico para a ultima posição que foi analisada pelo
            automato de operadores lógicos, para que a verificação continue de onde o automato parou.*/
            setPosicao(operadoresLogicos.getPosicaoFinal());
            //Adiciona no token o número da linha que o token estava no arquivo de entrada.
            resultadoOperadoresLogicos.setLinha(this.linhaAtual);

            if(resultadoOperadoresLogicos.isError()){
                this.erros.add(resultadoOperadoresLogicos);
            }else {
                this.tokens.add(resultadoOperadoresLogicos);
            }
        }
        // A mesma lógica se repete para os outros automatos.
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
            if(resultadoOperadoresAritmeticos.isError()){
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
            if(resultadoDelimitadores.isError()){
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
            if(resultadoOperadoresRelacionais.isError()){
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
            if(resultadoCadeiasCaracteres.isError()){
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
            if(resultadoPalavrasReservadasIdentificadores.isError()){
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
            if(resultadoNumeros.isError()){
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
            /*Como o caractere analisado não pertence a nenhum padrão de token, ou do alfabeto da linguagem, o mesmo é
            è interpretado como erro do tipo CNPA, Caractere que não pertence ao alfabeto*/
            if (this.posicao < linha.length()){
                char auxiliar = linha.charAt(posicao);
                //Verifica se o caractere é uma tabulação ou um espaço, caso seja, é ignorado.
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

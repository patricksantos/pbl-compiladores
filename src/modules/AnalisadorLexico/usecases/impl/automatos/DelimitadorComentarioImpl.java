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
        this.error = false;
        String lexema = ""; // Lexema que irá ser retornado
        String result = estadoInicial(arquivo, texto, linha, posicao, lexema);

        if(this.error){ // Verifica se o retorno foi um caracatere mal formado e entra nessa condição
            return new Token("CoMF", result, true);
        }
        else if(result == null) { // O caractere analisado não pertence a esse autômato
            return null;
        }
        else { // Ele identifica como um comentario e retorna um token que não será utilizado o retorno serve apenas para verificar se o automato identificou um comentario ou não
            return new Token("Comentario", result, false);
        }
    }

    @Override
    public Token getToken(String texto, int posicao) { // Não irá ser utilizado
        return null;
    }

    /** ---- Estados ---- **/

    /**
     * Método que retona o token identificado,caso o mesmo for encontrado.
     * @param arquivo arquivo de entrada para verificar os casos onde precisa passar para a proxima linha
     * @param texto uma linha do arquivo de entrada
     * @param linha a linha que irá começar a leitura
     * @param posicao a posição na linha que irá começar a leitura
     * @param lexema o lexema do token
     * @return o token identificado, ou null caso o mesmo não for encontrado.
     * */
    private String estadoInicial(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '/' ){ // Verifica se o primeiro caractere é uma / para iniciar a verificação
            return estadoA(arquivo, texto, linha, posicao + 1, lexema);
        }

        return null; // Se não ele retorna NULL informando que não é um comentario
    }

    private String estadoA(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){

        if( posicao >= texto.length() ){
            return null;
        }

        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '*' && texto.charAt(posicao - 1) == '/' ) // Se o caractere for um * e o anterior for um / então ele passa para o proximo estado
        {

            if( posicao + 1 >= texto.length() ) // Ele identifica se ja esta no final da linha e passa para a proxima
            {
                linha += 1;
                texto = arquivo.get(linha);

                return estadoB(arquivo, texto, linha, 0, lexema);
            }
            else{

                return estadoB(arquivo, texto, linha, posicao + 1, lexema);
            }
        }
        else if( c == '/' && texto.charAt(posicao - 1) == '/' ){ // Se o caractere for um / e o anterior for um / então ele passa para o proximo estado referente ao segundo tipo de comentario
            return estadoD(linha, texto, posicao + 1, lexema);
        }

        return null; // Se não ele retorna NULL informando que não é um comentario
    }

    private String estadoB(ArrayList<String> arquivo, String texto, int linha, int posicao, String lexema){
        StringBuilder lexemaBuilder = new StringBuilder(lexema);

        if( posicao >= texto.length()){ // Ele verifica se a linha é vazia

            if( linha + 1 >= arquivo.size() ) { // Se for o final do arquivo ele retorna o Lexema e identifica que foi um comentario mal formado
                setPosicaoFinal(posicao);
                setLinhaFinal(arquivo.size() + 1);
                this.error = true;

                return lexemaBuilder.toString();
            }

            linha = linha + 1;
            texto = arquivo.get(linha);

            while (texto.isEmpty()){ // Se for uma linha vazia ele passa para a proxima até achar uma linha não vazia
                linha = linha + 1;
                texto = arquivo.get(linha);
            }

            return estadoB(arquivo, texto, linha, 0, lexemaBuilder.toString()); // E depois retorna o mesmo estado para fazer a msm verificação
        }

        for (int i = posicao; i < texto.length(); i++) // Ele irá percorrer o texto depois do /* até achar o * e passar para o proximo estado
        {
            char c = texto.charAt(i);
            if(c == '*') { // Condição para o proximo estado

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

        if( c == '/' ) { // Se ele entrar o / vai retornar o estado final pois o comentario esta terminado

            return estadoFinal(linha, posicao + 1, lexema);
        }
        else { // Se não ele volta para o estado anterior pois ainda não acabou o comentario
            return estadoB(arquivo, texto, linha, posicao + 1, lexema);
        }
    }

    private String estadoD(int linha, String texto, int posicao, String lexema){
        StringBuilder lexemaBuilder = new StringBuilder(lexema);

        for (int i = posicao; i < texto.length(); i++) // Ele analisa toda a linha e quando chega no final ele retorna o estado final
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
        setPosicaoFinal(posicao); // Ele seta a posição + 1 que terminou a analise
        setLinhaFinal(linha); // Ele retorna a linha que terminou a analise

        return lexema;
    }

    /** ---- Metodos Gerais ---- **/

     /**
     * @return a posição que o analisador léxico irá analisar, depois de sair desse automato
     * */
    @Override
    public int getPosicaoFinal(){
        return this.posicaoFinal;
    }

     /**
     * Atualiza a posição final
     * @param posicaoFinal a nova posição final
     * */
    @Override
    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

     /**
     * @return a linha em que o analisador léxico irá analisar, depois de sair desse automato
     * */
    @Override
    public int getLinhaFinal() {
        return this.linhaFinal;
    }

     /**
     * Atualiza a linha final
     * @param linhaFinal a nova linha final
     * */
    @Override
    public void setLinhaFinal(int linhaFinal) {
        this.linhaFinal = linhaFinal;
    }
}

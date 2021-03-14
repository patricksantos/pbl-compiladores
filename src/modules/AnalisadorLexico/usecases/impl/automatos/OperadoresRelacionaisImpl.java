package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresRelacionais;
/**
 * Classe responsável por representar o automato que identifica os tokens do tipo operador relacional
 * */
public class OperadoresRelacionaisImpl implements IOperadoresRelacionais {
    private int posicaoFinal;/*Proxima posição da linha que o analisador léxico irá analisar, para que a classe
    AnalisadorLéxico continue a analise de onde este automato parou.*/
    /**
     * Método que retona o token identificado, caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @return o token identificado, ou null caso o mesmo não for encontrado.
     * */
    @Override
    public Token getToken(String texto, int posicao) {

        String lexema = ""; // Lexema que irá ser retornado
        return estadoInicial(texto, posicao, lexema);
    }

    /** ---- Estados ---- **/
    /**
     * Método que retona o token identificado,caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @param lexema o lexema do token
     * @return o token identificado, ou null caso o mesmo não for encontrado.
     * */
    private Token estadoInicial(String texto, int posicao, String lexema){

        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '>' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(c == '<'){
            return estadoB(texto, posicao + 1, lexema);
        }
        else if(c == '!'){
            return estadoC(texto, posicao + 1, lexema);
        }
        else if(c == '='){
            return estadoD(texto, posicao + 1, lexema);
        }
        //Caso o caractere encontrado não pertença a esse automato
        return null;
    }

    private Token estadoA(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                //Token >= encontrado
                return estadoFinal(posicao + 1, lexema);
            } else {
                /*Como o caractere lido não era o esperado, o mesmo será lido por outro automato. Por isso a posiçãp
                não foi  incrementada*/
                //Token > encontrado
                return estadoFinal(posicao, lexema);
            }
        }
        //Token > encontrado
        return estadoFinal(posicao, lexema);
    }

    private Token estadoB(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                //Token <= encontrado
                return estadoFinal(posicao + 1, lexema);
            } else {
                /*Como o caractere lido não era o esperado, o mesmo será lido por outro automato. Por isso a posiçãp
                não foi  incrementada*/
                //Token < encontrado
                return estadoFinal(posicao, lexema);
            }
        }
        //Token < encontrado
        return estadoFinal(posicao, lexema);
    }

    private Token estadoC(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                //token != encontrado
                return estadoFinal(posicao + 1, lexema);
            } else {
                /*o proximo caractere que iria formar o token não foi o esperado, por isso o token não foi formado,e a
                posição é decrmentada pra quer o caractere seja lido por outro automato*/
                setPosicaoFinal(posicao-1);
                return null;
            }
        }
        //Como a linha terminou, o token não foi formado
        setPosicaoFinal(posicao-1);
        return null;
    }

    private Token estadoD(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '=') {
                lexema += c;
                //token == encontrado
                return estadoFinal(posicao + 1, lexema);
            } else {
                /*Como o caractere lido não era o esperado, o mesmo será lido por outro automato. Por isso a posiçãp
                não foi  incrementada*/
                //Token = encontrado
                return estadoFinal(posicao, lexema);
            }
        }
        //Token = encontrado
        return estadoFinal(posicao, lexema);
    }
    /**
     * Método que representa o estado final do automato, e retorna o token encontrado
     * @param posicao que será adicionada como posição final do automato
     * @param lexema lexema do token identificado
     * @return o token identificado
     * */
    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("REL",lexema, false);
    }
    /**
     * Método herdado da interface.
     * */
    @Override
    public void getError() {

    }
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
    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }
}

package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IOperadoresLogicos;
/**
 * Classe responsável por representar o automato que identifica os tokens do tipo operador lógico
 * */
public class OperadoresLogicosImpl implements IOperadoresLogicos {

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

        if( c == '&' ){
            return estadoA(texto, posicao + 1, lexema);
        }
        else if(c == '|'){
            return estadoB(texto, posicao + 1, lexema);
        }
        else if(c == '!'){
            return estadoC(texto,posicao + 1, lexema);
        }
        //Caso o caractere encontrado não pertença a esse automato
        return null;
    }

    private Token estadoA(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '&') {
                lexema += c;
                //Token && encontrado
                return estadoFinal(posicao + 1, lexema);
            }
        }

        /*Como o caractere lido não era o esperado, o mesmo será lido por outro automato. Por isso a posiçãp
        não foi  incrementada*/
        setPosicaoFinal(posicao);
        //Como o operador lógico não foi formado corretamente é retornado um token de erro.
        return new Token("OpMF",lexema,true);
    }

    private Token estadoB(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            if (c == '|') {
                lexema += c;
                //Token || encontrado
                return estadoFinal(posicao + 1, lexema);
            }
        }

        /*Como o caractere lido não era o esperado, o mesmo será lido por outro automato. Por isso a posiçãp
        não foi  incrementada*/
        setPosicaoFinal(posicao);
        //Como o operador lógico não foi formado corretamente é retornado um token de erro.
        return new Token("OpMF",lexema,true);
    }

    private Token estadoC(String texto, int posicao, String lexema){
        //Verifica se a linha terminou
        if(posicao < texto.length()) {
            char c = texto.charAt(posicao);
            //Verifica se o ! faz parte do operador relacional !=
            if (c == '=') {
                //retona a posição do ! para que o token possa ser lido pelo automato correto
                setPosicaoFinal(posicao - 1);
                return null;
            }
        }
        //Token ! encontrado
        return estadoFinal(posicao + 1, lexema);
    }
    /**
     * Método que representa o estado final do automato, e retorna o token encontrado
     * @param posicao que será adicionada como posição final do automato
     * @param lexema lexema do token identificado
     * @return o token identificado
     * */
    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("LOG", lexema,false);
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
}



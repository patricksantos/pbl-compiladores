package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAutomato;
import modules.AnalisadorLexico.usecases.facades.automatos.ISimbolo;
/**
 * Classe responsável por representar o automato que identifica os simbolos mal formados
 * */
public class SimbolosImpl implements ISimbolo {
    private int posicaoFinal;/*Proxima posição da linha que o analisador léxico irá analisar, para que a classe
    AnalisadorLéxico continue a analise de onde este automato parou.*/
    /**
     * Método que retona o token de erro so simbolo mal formado, caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @return o token de erro encontrado.
     * */
    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = ""; // Lexema que irá ser retornado
        return estadoInicial(texto, posicao, lexema);
    }

    /**
     * Método que retona o token de erro do símbolo mal formado, caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @param lexema o lexema do token de erro
     * @return o token de erro encontrado., ou null caso o mesmo não for encontrado.
     * */
    private Token estadoInicial(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        //Verifica se está nas posições aceitas da tabela ASCII
        if(texto.charAt(posicao) == '!' ||
                (texto.charAt(posicao) >= '#' && texto.charAt(posicao) <= '~') ) {
            return estadoFinal(posicao + 1, lexema);
        }

        return null;
    }

    /**
     * Método que representa o estado final do automato, e retorna o token de erro encontrado
     * @param posicao que será adicionada como posição final do automato
     * @param lexema lexema do token identificado
     * @return o token de erro identificado
     * */
    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("SIB",lexema,true);
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
    public int getPosicaoFinal() {
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

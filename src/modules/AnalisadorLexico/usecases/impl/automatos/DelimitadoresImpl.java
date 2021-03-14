package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IDelimitadores;
/**
 * Classe responsável por representar o automato que identifica os tokens do tipo delimitadores
 * */
public class DelimitadoresImpl implements IDelimitadores {

    private int posicaoFinal;/*Proxima posição da linha que o analisador léxico irá analisar, para que a classe
    AnalisadorLéxico continue a analise de onde este automato parou.*/
    /**
     * Método que retona o token identificado,caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @return o token identificado, ou null caso o mesmo não for encontrado.
     * */
    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = ""; // Lexema que irá ser retornado
        return estadoInicialFinal(texto, posicao, lexema);
    }

    /** ---- Estados ---- **/

    private Token estadoInicialFinal(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        //adiciona o caractere que foi lido ao lexema do token
        lexema += c;
        if( c == '.' || c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']' || c == ';' || c == ',' ){
            //Atualiza a posição, pois o caractere dessa posição já foi lido
            setPosicaoFinal(posicao + 1);
            return new Token("DEL", lexema,false);
        }
        //Nenhum token foi identificado, por isso é retornado null
        return null;
    }
    /**
     * Método que retorna a ultima posição que foi lida pelo automato.
     * @return a posição final.
     * */
    @Override
    public int getPosicaoFinal(){
        return this.posicaoFinal;
    }

    /**
     * Método herdado da interface.
     * */
    @Override
    public void getError() {

    }
    /**
     * Método que atualiza a ultima posição que foi lida pelo automato.
     * @param posicaoFinal a posição atualizada.
     * */
    private void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

}

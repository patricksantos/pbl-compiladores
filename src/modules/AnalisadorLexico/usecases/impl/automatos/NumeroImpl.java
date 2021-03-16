package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.INumero;

public class NumeroImpl implements INumero {
    private int posicaoFinal;

    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = ""; // Lexema que irá ser retornado

        return this.estadoA(texto, posicao, lexema); // Retorna o estado inicial
    }

    /** ---- Estados ---- **/

    private Token estadoA(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        // Verificar se é realmente um numero e passa para o proximo estado
        if( c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ){
            return this.estadoB(texto, posicao + 1, lexema);
        }

        return null; // Se não ele retorna NULL informando que não é um número
    }

    private Token estadoB(String texto, int posicao, String lexema){

        if( posicao >= texto.length() ){ // Se for final de linha ele retorna um numero com um digito
            setPosicaoFinal(posicao);
            return new Token("NRO", lexema,false);
        }

        for(int i = posicao; i < texto.length(); i++){ // Ele vai analisando os caracteres e retorna o Token
            char c = texto.charAt(i);

            if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ){
                lexema += c;
                if( i + 1 == texto.length() )
                {
                    setPosicaoFinal(i + 1);
                    return new Token("NRO", lexema, false);
                }
            }
            else if( c == '.'){ // Se for um ponto ele passa para o proximo estado
                lexema += c;
                return this.estadoC(texto, i + 1, lexema);
            }else { // Se for algum caractere que não seja um numero ele retorna o Token
                setPosicaoFinal(i);
                return new Token("NRO", lexema, false);
            }
        }

        return null; // Se não ele retorna NULL informando que não é um comentario
    }

    private Token estadoC(String texto, int posicao, String lexema){
        // Se for fim de linha e não tiver um numero dps do ponto ele retona o Token e informa que é um numero mal formado
        if( posicao >= texto.length()){
            setPosicaoFinal(posicao);
            return new Token("NMF", lexema, true);
        }

        String c = texto.substring(posicao, posicao + 1);

        if( c.matches("[0-9]") ){ // Se vier um numero depois do ponto ele retorna o proximo estado
            lexema += c;
            return this.estadoD(texto, posicao + 1, lexema);
        }
        else if( c.matches(".") ) { // Se vier outro ponto em sequencia ele retona o Token e informa que é um numero mal formado
            lexema += c;
            setPosicaoFinal(posicao + 1);
            return new Token("NMF", lexema, true);
        }

        return null; // Se não ele retorna NULL informando que não é um número
    }

    private Token estadoD(String texto, int posicao, String lexema){
        if( posicao >= texto.length() ){ // Se for final de linha ele retorna o lexema com apenas um numero apos o ponto
            setPosicaoFinal(posicao);
            return new Token("NRO", lexema, false);
        }

        for(int i = posicao; i < texto.length(); i++){ // Ele irá verificando os caracteres e se for numero ele add no Lexema
            String c = texto.substring(i, i + 1);

            if( c.matches("[0-9]") ){
                lexema += c;
                if( i + 1 == texto.length() ) // Se for final de linha ele retorna o Token
                {
                    setPosicaoFinal(i + 1);
                    return new Token("NRO", lexema, false);
                }
            }
            else { // Se vier algum caractere diferente de um numero ele retona o Token
                setPosicaoFinal(i);
                return new Token("NRO", lexema, false);
            }
        }

        return null; // Se não ele retorna NULL informando que não é um número
    }

    /** ---- Metodos Gerais ---- **/

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
    @Override
    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }
}

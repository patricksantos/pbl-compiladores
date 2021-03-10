package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.INumero;

public class NumeroImpl implements INumero {
    private int posicaoFinal;

    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = "";

        return this.estadoA(texto, posicao, lexema);
    }

    /** ---- Estados ---- **/

    private Token estadoA(String texto, int posicao, String lexema){
        char c = texto.charAt(posicao);
        lexema += c;

        if( c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ){
            return this.estadoB(texto, posicao + 1, lexema);
        }

        return null;
    }

    private Token estadoB(String texto, int posicao, String lexema){

        if( posicao >= texto.length() ){
            setPosicaoFinal(posicao);
            return new Token("NRO", lexema,false);
        }

        for(int i = posicao; i < texto.length(); i++){
            char c = texto.charAt(i);
            System.out.println(lexema);

            if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' ){
                lexema += c;
                if( i + 1 == texto.length() )
                {
                    setPosicaoFinal(i + 1);
                    return new Token("NRO", lexema, false);
                }
            }
            else if( c == '.'){
                lexema += c;
                return this.estadoC(texto, i + 1, lexema);
            }else {
                setPosicaoFinal(i);
                return new Token("NRO", lexema, false);
            }
        }

        return null;
    }

    private Token estadoC(String texto, int posicao, String lexema){
        if( posicao >= texto.length()){
            setPosicaoFinal(posicao - 1);
            return new Token("NRO", lexema.replace(".", ""), false);
        }

        String c = texto.substring(posicao, posicao + 1);

        if( c.matches("[0-9]") ){
            lexema += c;
            return this.estadoD(texto, posicao + 1, lexema);
        }
        else if( c.matches(".") ) {
            setPosicaoFinal(posicao - 1);
            return new Token("NRO", lexema.replace(".", ""),false);
        }

        return null;
    }

    private Token estadoD(String texto, int posicao, String lexema){
        if( posicao >= texto.length() ){
            setPosicaoFinal(posicao);
            return new Token("NRO", lexema, false);
        }

        for(int i = posicao; i < texto.length(); i++){
            String c = texto.substring(i, i + 1);

            if( c.matches("[0-9]") ){
                lexema += c;
                if( i + 1 == texto.length() )
                {
                    setPosicaoFinal(i + 1);
                    return new Token("NRO", lexema, false);
                }
            }
            else {
                setPosicaoFinal(i);
                return new Token("NRO", lexema, false);
            }
        }

        return null;
    }

    /** ---- Metodos Gerais ---- **/

    @Override
    public void getError() {

    }

    @Override
    public int getPosicaoFinal() {
        return this.posicaoFinal;
    }

    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }
}

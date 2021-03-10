package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IPalavrasReservadasIdentificadores;

public class PalavrasReservadasIdentificadoresImpl implements IPalavrasReservadasIdentificadores {
    private int posicaoFinal;

    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = "";

        return estadoA(texto, posicao, lexema);

    }

    /** ---- Estados ---- **/

    private Token estadoA(String texto, int posicao, String lexema){
        String c = texto.substring(posicao, posicao + 1);
        lexema += c;

        if(c.matches("[a-zA-Z]")){
            return estadoB(texto, posicao + 1, lexema);
        }

        return null;
    }

    private Token estadoB(String texto, int posicao, String lexema){

        if( posicao >= texto.length() ){
            setPosicaoFinal(posicao);
            return new Token("IDE", lexema, false);
        }

        for(int i = posicao; i < texto.length(); i++){
            String c = texto.substring(i, i + 1);

            if(c.matches("[a-zA-Z]"))
            {
                lexema += c;
                if( i + 1 == texto.length() ){
                    setPosicaoFinal(i + 1);
                    if(isPalavraReservada(lexema))
                    {
                        return new Token("PRE", lexema, false);
                    }
                    return new Token("IDE", lexema, false);
                }
            }
            else if(c.matches("[0-9_]")){
                lexema += c;
                return estadoC(texto, i + 1, lexema);
            }
            else{
                setPosicaoFinal(i);
                if(isPalavraReservada(lexema))
                {
                    return new Token("PRE", lexema, false);
                }
                return new Token("IDE", lexema, false);
            }
        }

        return null;
    }

    private Token estadoC(String texto, int posicao, String lexema){

        if( posicao >= texto.length() ){
            setPosicaoFinal(posicao);
            return new Token("IDE", lexema, false);
        }

        for(int i = posicao; i < texto.length(); i++){
            String c = texto.substring(i, i + 1);

            if( c.matches("\\w") ){
                lexema += c;
                if( i + 1 == texto.length() ){
                    setPosicaoFinal(i + 1);
                    return new Token("IDE", lexema,false);
                }
            }
            else {
                setPosicaoFinal(i + 1);
                return new Token("IDE", lexema, false);
            }
        }

        return null;
    }

    /** ---- Metodos Gerais ---- **/

//    private boolean isPalavraReservada(String lexema){
//        String regexPalavraReservada = "[var | const | typedef | struct | extends | procedure | function | start | return | if | else | then | while | read | print | int | real | boolean | string | true | false | global | local]";
//        Pattern pattern = Pattern.compile(regexPalavraReservada);
//        Matcher matcher = pattern.matcher(lexema);
//        return matcher.find();
//    }

    private boolean isPalavraReservada(String lexema){
        return lexema.matches("var") || lexema.matches("const") || lexema.matches("typedef") || lexema.matches("struct") || lexema.matches("extends") || lexema.matches("function") || lexema.matches("procedure")
                || lexema.matches("start") || lexema.matches("return") || lexema.matches("if") || lexema.matches("else") || lexema.matches("then") || lexema.matches("while") || lexema.matches("read") || lexema.matches("print")
                || lexema.matches("int") || lexema.matches("real") || lexema.matches("boolean") || lexema.matches("string") || lexema.matches("true") || lexema.matches("false") || lexema.matches("global")
                || lexema.matches("local");
    }

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

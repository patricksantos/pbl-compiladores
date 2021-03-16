package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.IPalavrasReservadasIdentificadores;

public class PalavrasReservadasIdentificadoresImpl implements IPalavrasReservadasIdentificadores {
    private int posicaoFinal;

    @Override
    public Token getToken(String texto, int posicao) {
        String lexema = ""; // Lexema que irá ser retornado

        return estadoA(texto, posicao, lexema); // Retorna o estado inicial

    }

    /** ---- Estados ---- **/

    /**
     * Método que retona o token identificado,caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @param lexema o lexema do token
     * @return o token identificado, ou null caso o mesmo não for encontrado.
     * */
    private Token estadoA(String texto, int posicao, String lexema){
        String c = texto.substring(posicao, posicao + 1);
        lexema += c;

        if(c.matches("[a-zA-Z]")){ // Ele identifica se o caracatere é um e retorna o proximo estado
            return estadoB(texto, posicao + 1, lexema);
        }

        return null; // Se não ele retorna NULL informando que não é uma Palavra reservada ou identificador
    }

    private Token estadoB(String texto, int posicao, String lexema){

        if( posicao >= texto.length() ){ // Se terminar a linha e ele retorna um identificador só com uma letra
            setPosicaoFinal(posicao);
            return new Token("IDE", lexema, false);
        }

        for(int i = posicao; i < texto.length(); i++){ // Ele analisa os proximo caracateres
            String c = texto.substring(i, i + 1);

            if(c.matches("[a-zA-Z]")) // Se for uma letra ele irá adicionando no lexema
            {
                lexema += c;
                if( i + 1 == texto.length() ){ // Quando chegar no fim de linha ele irá analisar se é uma IDE ou PRE
                    setPosicaoFinal(i + 1);
                    if(isPalavraReservada(lexema)) // Se retornar true o lexema é uma palavra reservada
                    {
                        return new Token("PRE", lexema, false);
                    }

                    return new Token("IDE", lexema, false); // Se não é um identificador
                }
            }
            else if(c.matches("[0-9_]")){ // Se surgir alguma Letra então ele poderá ser apaenas um identificador
                lexema += c;
                return estadoC(texto, i + 1, lexema); // Passa para o estado final de identificador
            }
            else{ // Se não for nenhuma das duas condições anteriores ele encera o loop e analisa se ele é uma IDE ou PRE
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

        if( posicao >= texto.length() ){ // Se for o final da linha ele retorna o token de um identificador
            setPosicaoFinal(posicao);
            return new Token("IDE", lexema, false);
        }

        // Ele irá percorrer os demais caracteres até chegar a um caractere desconhecido pelo autômato ou final de linha
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

    /**
     * @return se o lexema é uma palavra reservada ou não
     * */
    private boolean isPalavraReservada(String lexema){
        return lexema.matches("var") || lexema.matches("const") || lexema.matches("typedef") || lexema.matches("struct") || lexema.matches("extends") || lexema.matches("function") || lexema.matches("procedure")
                || lexema.matches("start") || lexema.matches("return") || lexema.matches("if") || lexema.matches("else") || lexema.matches("then") || lexema.matches("while") || lexema.matches("read") || lexema.matches("print")
                || lexema.matches("int") || lexema.matches("real") || lexema.matches("boolean") || lexema.matches("string") || lexema.matches("true") || lexema.matches("false") || lexema.matches("global")
                || lexema.matches("local");
    }

    /**
     * @return a posição que o analisador léxico irá analisar, depois de sair desse automato
     * */
    @Override
    public int getPosicaoFinal() {
        return this.posicaoFinal;
    }

    /**
     * Atualiza a linha final
     * @param posicaoFinal a nova posição final
     * */
    @Override
    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

}

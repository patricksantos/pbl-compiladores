package modules.AnalisadorLexico.usecases.impl.automatos;

import domain.entities.Token;
import modules.AnalisadorLexico.usecases.facades.automatos.ICadeiasCaracteres;
/**
 * Classe responsável por representar o automato que identifica os tokens do tipo cadeias de caracteres
 * */
public class CadeiasCaracteresImpl implements ICadeiasCaracteres {

    private int posicaoFinal;/*Proxima posição da linha que o analisador léxico irá analisar, para que a classe
    AnalisadorLéxico continue a analise de onde este automato parou.*/
    private int posicaoInicial;//O inicio da formação de uma cadeia de caracteres
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

    /**
     * Método que retona o token identificado,caso o mesmo for encontrado.
     * @param texto uma linha do arquivo de entrada
     * @param posicao a posição na linha que irá começar a leitura
     * @param lexema o lexema do token
     * @return o token identificado, ou null caso o mesmo não for encontrado.
     * */
    private Token estadoInicial(String texto, int posicao, String lexema){
        //Pega o caractere da posição requerida
        char c = texto.charAt(posicao);
        lexema += c;

        //caracteriza o inicio de uma cadeia de caracteres, então os estados dos automatos são chamados.
        if( c == '"' ){
            //Caso ocorra algum erro, a posição onde começa a cadeia de caracteres é salva
            this.posicaoInicial = posicao;
            //é adicionado 1 a posição para que o proximo caractere seja lido
            return estadoA(texto, posicao + 1, lexema);
        }

        return null;
    }

    private Token estadoA(String texto, int posicao, String lexema){
         boolean erro = false;
        //Verifica se a linha acabou
        if(posicao < texto.length()) {
            //verifica se o usuário fechou as aspas, pegando o texto após a abertura das aspas.
            if(!texto.substring(posicao).contains("\"") ){
                //É retonado toda a linha após a abertura das "
                this.setPosicaoFinal(texto.length());
                return new Token("CMF",texto.substring(this.posicaoInicial),true);
            }

            char c = texto.charAt(posicao);
            //Enquanto não achar o fim da cadeia de caracteres ou o fim de linha ele continua lendo os caracteres
            while (c != '"' && posicao < texto.length()) {

                //Confere se está nas regras de formação de uma cadeia de caracteres
                if((texto.charAt(posicao) >= ' ' && texto.charAt(posicao) <= '!') ||
                        (texto.charAt(posicao) >= '#' && texto.charAt(posicao) <= '~') ) {
                    //Verifica a ocorrencia de \" na cadeia de caracteres"
                    if(c == '\\'){
                        lexema += c;
                        //Como um caractere foi lido, a posição é incrementada
                        posicao++;
                        if(posicao < texto.length()){
                            char auxiliar = texto.charAt(posicao);
                            if(auxiliar == '"'){
                                lexema += auxiliar;
                                posicao++;
                            }
                        }
                    }else {
                        lexema += c;
                        posicao++;
                    //Verifica o fim de linha
                    }if(posicao < texto.length()) {
                        c = texto.charAt(posicao);
                    }
                }else{
                    /*Caso a cadeia tenha algum caractere que não pertença as regras de sua formação, a mesma é uma
                    cadeia mal formada*/
                    //this.setPosicaoFinal(texto.length());
                    //return new Token("CMF",texto.substring(this.posicaoInicial),true);
                    erro = true;
                    lexema += c;
                    posicao++;
                    if(posicao < texto.length()) {
                        c = texto.charAt(posicao);
                    }
                }
            }
            /*Verifica se a condição de quebra do while oi o fim de linha,se sim, é uma cadeia mal formada, pois o "
            não foi encontrado*/
            lexema += c;
            if(posicao >= texto.length()){
                this.setPosicaoFinal(texto.length());
                return new Token("CMF",texto.substring(this.posicaoInicial),true);
            /*Caso a cadeia de caracteres esteja entre parenteses, mas algum dos caracteres da cadeia não obedece as
            regras de formação da mesma*/
            }else if(erro){
                this.setPosicaoFinal(posicao+1);
                return new Token("CMF",lexema,true);
            }
            /*condição de quebra do while foi a identificação do outro ", logo o mesmo é adicionado ao lexema, e o token
            é retornado*/

            /*A posição é incrementada pois quando sair desse automato e retomar a leitura da linha, pelo analisador
            léxico, o mesmo  irá começar do primeiro caractere após o ultimo que foi lido pelo automato*/
            return estadoFinal(posicao + 1, lexema);
        }
        //caso a linha tenha acabado, só existe o ", logo é uma cadeia mal formada
        this.setPosicaoFinal(texto.length());
        return new Token("CMF",texto.substring(this.posicaoInicial),true);
    }
    /**
     * Método que representa o estado final do automato, e retorna o token encontrado
     * @param posicao que será adicionada como posição final do automato
     * @param lexema lexema do token identificado
     * @return o token identificado
     * */
    private Token estadoFinal(int posicao, String lexema){
        setPosicaoFinal(posicao);
        return new Token("CAD", lexema,false);
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
    @Override
    public void setPosicaoFinal(int posicaoFinal) {

        this.posicaoFinal = posicaoFinal;
    }
}

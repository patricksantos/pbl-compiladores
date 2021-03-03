
/**
 * Classe que representa um token
 * */
public class Token {
    private String tipo; //Tipo de token identificado
    private String lexema; //lexema do token
    private int idTabela; //caso o token for um identificador, o id dele na tabela de símbolos;

    /**
     * @param tipo tipo do token que foi identificado
     * @param lexema lexema do token
     * */
    public Token(String tipo, String lexema){
        this.lexema = lexema;
        this.tipo = tipo;
    }

    /**
     * Sobreescrita de método caso o token for um identificador ou palavra reservada
     * @param tipo tipo do token que foi identificado
     * @param lexema lexema do token
     * @param idTabela posição do token na tabela de símbolos
     * */
    public Token(String tipo, String lexema, int idTabela){
        this.lexema = lexema;
        this.tipo = tipo;
    }

    /**
     * @return o tipo do token
     * */
    public String getTipo(){
        return this.tipo;
    }

    /**
     * @return o lexema do token
     * */
    public String getLexema(){
        return this.lexema;
    }

    /**
     * @return o id na tabela de símbolos do token
     * */
    public int getIdTabela(){
        return this.idTabela;
    }

    /**
     * @return a forma que o token será escrito no arquivo
     * */
    public String info(){
        return "<"+ this.tipo + "," + this.lexema + ">";
    }

}

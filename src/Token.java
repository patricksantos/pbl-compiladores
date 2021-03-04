
/**
 * Classe que representa um token
 * */
public class Token {
    private String tipo; //Tipo de token identificado
    private String lexema; //lexema do token ou id ta tabela de símbolo se for um token identificador

    /**
     * @param tipo tipo do token que foi identificado
     * @param lexema lexema do token ou id ta tabela de símbolo se for um token identificador
     * */
    public Token(String tipo, String lexema){
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
     * @return a forma que o token será escrito no arquivo
     * */
    public String info(){
        return "<"+ this.tipo + "," + this.lexema + ">";
    }

}

package domain.entities;

/**
 * Classe que representa um token
 * */
public class Token {
    private String tipo; //Tipo de token identificado
    private String lexema; //lexema do token ou id ta tabela de símbolo se for um token identificador
    private String linha; //Número da linha onde ocorreu o token
    private boolean erro;

    /**
     * @param tipo tipo do token que foi identificado
     * @param lexema lexema do token ou id ta tabela de símbolo se for um token identificador
     * */
    public Token(String tipo, String lexema, boolean erro){
        this.lexema = lexema;
        this.tipo = tipo;
        this.erro = erro;
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
     * @param linha número da linha onde ocorreu o token
     * */
    public void setLinha(int linha){
        linha++;
        if(linha < 10){
            this.linha = "0" + linha;
        }
        else{
            this.linha = "" + linha;
        }

    }

    public void setErro(boolean erro){
        this.erro = erro;
    }

    public boolean getError(){
        return this.erro;
    }

    /**
     * @return a forma que o token será escrito no arquivo
     * */
    public String info(){
        return this.linha + " " + this.tipo + " " + this.lexema;
    }

}

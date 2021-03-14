package domain.entities;

/**
 * Classe que representa um token
 * */
public class Token {
    private String tipo; //Tipo de token identificado
    private String lexema; //lexema do token
    private String linha; // Número da linha onde ocorreu o token
    private boolean erro; // Indica se é um token de erro

    /**
     * @param tipo tipo do token que foi identificado
     * @param lexema lexema do token ou id ta tabela de símbolo se for um token identificador
     * @param erro identifica se é um token de erro
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
     * Método utilizado para adicionar a linha do arquivo de entrada onde ocorreu o token
     * @param linha número da linha onde ocorreu o token
     * */
    public void setLinha(int linha){
        //é adicionado 1 para começar a contagem do 1
        linha++;
        if(linha < 10){
            this.linha = "0" + linha;
        }
        else{
            this.linha = "" + linha;
        }

    }
    /**
     * @param erro indica se o token é de erro ou não
     * */
    public void setErro(boolean erro){
        this.erro = erro;
    }

    /**
     * @return true caso seja um token de erro, e false caso o contrario.
     * */
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

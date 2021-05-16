package domain.entities;

import domain.error.IError;
import modules.AnalisadorSintatico.usecases.ErroSintatico;

/**
 * Classe que representa um token
 * */
public class Token {
    private String tipo; //Tipo de token identificado
    private String lexema; //lexema do token
    private String linha; // Número da linha onde ocorreu o token
    private boolean isError; // Indica se é um token de erro
    private IError error; // Indica se é um token de erro
    private boolean erroSintatico;

    /**
     * @param tipo tipo do token que foi identificado
     * @param lexema lexema do token ou id ta tabela de símbolo se for um token identificador
     * @param isError identifica se é um token de erro
     * */
    public Token(String tipo, String lexema, boolean isError){
        this.lexema = lexema;
        this.tipo = tipo;
        this.isError = isError;
        this.erroSintatico = false;
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
     * @param error indica se o token é de erro ou não
     * */
    public void setError(IError error){
        this.error = error;
    }

    public IError getError() {
        return this.error;
    }

    /**
     * @return true caso seja um token de erro, e false caso o contrario.
     * */
    public boolean isError(){
        return this.isError;
    }

    public void setErroSintatico(boolean erroSin){
        this.erroSintatico = erroSin;
    }

    /**
     * @return a forma que o token será escrito no arquivo
     * */
    public String info(){
        if(erroSintatico){
            return this.error.info();
        }else {
            return this.linha + " " + this.tipo + " " + this.lexema;
        }
    }

    public String getLinha(){
        return this.linha;
    }

}

package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IIdentificador;

public class IdentificadorImpl implements IIdentificador {
    private int id;
    private Token token;
    private int escopo;

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public Token getToken(){
        return this.token;
    }

    public void setEscopo(int escopo){
        this.escopo = escopo;
    }

    public int getEscopo(){
        return this.escopo;
    }
}

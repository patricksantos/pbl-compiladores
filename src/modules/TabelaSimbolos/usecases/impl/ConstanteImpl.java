package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IConstante;

public class ConstanteImpl extends IdentificadorImpl implements IConstante {

    private String tipoConstante;
    private IdentificadorImpl identificador;

    /*public ConstanteImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
    }*/

    public ConstanteImpl(int id, Token token, int escopo){
        super(id,token,escopo);
    }

    public void setTipoConstante(String tipoConstante){
        this.tipoConstante = tipoConstante;
    }

    public String getTipoConstante(){
        return this.tipoConstante;
    }

    public void setIdentificador(IdentificadorImpl identificador){
        this.identificador = identificador;
    }

    public  IdentificadorImpl getIdentificador(){
        return this.identificador;
    }
}

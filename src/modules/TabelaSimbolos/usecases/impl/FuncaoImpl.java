package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IFuncao;

import java.util.ArrayList;

public class FuncaoImpl extends IdentificadorImpl implements IFuncao {
    private String tipoRetorno;
    private int quantidadeParametros;
    private ArrayList<String> tiposParametros;
    private IdentificadorImpl identificador;

    /*public FuncaoImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
    }*/

    public FuncaoImpl(int id, Token token, int escopo){
        super(id,token,escopo);
    }

    public String getTipoRetorno(){
        return this.tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno){
        this.tipoRetorno = tipoRetorno;
    }

    public int getQuantidadeParametros(){
        return this.quantidadeParametros;
    }

    public void setQuantidadeParametros(int quantidadeParametros){
        this.quantidadeParametros = quantidadeParametros;
    }

    public ArrayList<String> getTiposParametros(){
        return this.tiposParametros;
    }

    public void setTiposParametros(ArrayList<String> tiposParametros){
        this.tiposParametros = tiposParametros;
    }

    public IdentificadorImpl getIdentificador(){
        return this.identificador;
    }

    public void setIdentificador(IdentificadorImpl identificador){
        this.identificador = identificador;
    }
}

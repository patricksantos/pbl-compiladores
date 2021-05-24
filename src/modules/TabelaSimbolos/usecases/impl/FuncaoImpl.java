package modules.TabelaSimbolos.usecases.impl;

import modules.TabelaSimbolos.usecases.facade.IFuncao;

import java.util.ArrayList;

public class FuncaoImpl extends IdentificadorImpl implements IFuncao {
    private String tipoRetorno;
    private int quantidadeParametros;
    private ArrayList<Integer> tiposParametros;
    private IdentificadorImpl identificador;

    public FuncaoImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
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

    public ArrayList<Integer> getTiposParametros(){
        return this.tiposParametros;
    }

    public void setTiposParametros(ArrayList<Integer> tiposParametros){
        this.tiposParametros = tiposParametros;
    }

    public IdentificadorImpl getIdentificador(){
        return this.identificador;
    }

    public void setIdentificador(IdentificadorImpl identificador){
        this.identificador = identificador;
    }
}

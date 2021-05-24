package modules.TabelaSimbolos.usecases.impl;

import modules.TabelaSimbolos.usecases.facade.IProcedimento;

import java.util.ArrayList;

public class procedimentoImpl extends IdentificadorImpl implements IProcedimento {
    private int quantidadeParametros;
    private ArrayList<Integer> tiposParametros;
    private IdentificadorImpl identificador;

    public procedimentoImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
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

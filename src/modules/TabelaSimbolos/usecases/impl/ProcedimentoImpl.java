package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IProcedimento;

import java.util.ArrayList;

public class ProcedimentoImpl extends IdentificadorImpl implements IProcedimento {
    private int quantidadeParametros;
    private ArrayList<String> tiposParametros;
    private IdentificadorImpl identificador;

    /*public ProcedimentoImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
    }*/

    public ProcedimentoImpl(int id, Token token, int escopo){
        super(id,token,escopo);
        this.tiposParametros = new ArrayList<>();
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
        for(String tipo: tiposParametros){
            this.tiposParametros.add(tipo);
        }
    }

    public IdentificadorImpl getIdentificador(){
        return this.identificador;
    }

    public void setIdentificador(IdentificadorImpl identificador){
        this.identificador = identificador;
    }

}

package modules.TabelaSimbolos.usecases.facade;

import modules.TabelaSimbolos.usecases.impl.IdentificadorImpl;

import java.util.ArrayList;

public interface IProcedimento extends IIdentificador{

    public int getQuantidadeParametros();

    public void setQuantidadeParametros(int quantidadeParametros);

    public ArrayList<String> getTiposParametros();

    public void setTiposParametros(ArrayList<String> tiposParametros);

    public IdentificadorImpl getIdentificador();

    public void setIdentificador(IdentificadorImpl identificador);
}

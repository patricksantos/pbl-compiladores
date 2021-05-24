package modules.TabelaSimbolos.usecases.facade;

import modules.TabelaSimbolos.usecases.impl.IdentificadorImpl;

import java.util.ArrayList;

public interface IFuncao {

    public String getTipoRetorno();

    public void setTipoRetorno(String tipoRetorno);

    public int getQuantidadeParametros();

    public void setQuantidadeParametros(int quantidadeParametros);

    public ArrayList<Integer> getTiposParametros();

    public void setTiposParametros(ArrayList<Integer> tiposParametros);

    public IdentificadorImpl getIdentificador();

    public void setIdentificador(IdentificadorImpl identificador);
}

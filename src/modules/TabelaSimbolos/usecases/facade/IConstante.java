package modules.TabelaSimbolos.usecases.facade;

import modules.TabelaSimbolos.usecases.impl.IdentificadorImpl;

public interface IConstante {
    public void setTipoConstante(String tipoConstante);

    public String getTipoConstante();

    public void setIdentificador(IdentificadorImpl identificador);

    public  IdentificadorImpl getIdentificador();
}

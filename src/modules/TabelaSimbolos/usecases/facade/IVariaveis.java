package modules.TabelaSimbolos.usecases.facade;

import modules.TabelaSimbolos.usecases.impl.IdentificadorImpl;

import java.util.ArrayList;
import java.util.HashMap;

public interface IVariaveis extends IIdentificador{

    public String getTipoVariavel();

    public void setTipoVariavel(String tipoVariavel);

    public String getModeloVariavel();

    public void setModeloVariavel(String modeloVariavel);

    public String getStructPai();

    public void setStructPai(String structPai);

    public int getMaxIndice();

    public void setMaxIndice(int maxIndice);

    public int getMaxIndice2();

    public void setMaxIndice2(int maxIndice2);

    public int getIndiceAtual();

    public void setIndiceAtual(int indiceAtual);

    public int getIndiceAtual2();

    public void setIndiceAtual2(int indiceAtual2);

    public IdentificadorImpl getIdentificador();

    public void setIdentificador(IdentificadorImpl identificador);

    public ArrayList<String> getTiposAtributosStruct();

    public void adicionarAtributosStruct(String lexema);

    public void adcionarTiposAtributosStruct(String tipo);

    public ArrayList<String> getAtributosStruct();
}

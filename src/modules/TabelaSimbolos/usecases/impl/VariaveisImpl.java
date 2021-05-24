package modules.TabelaSimbolos.usecases.impl;

import modules.TabelaSimbolos.usecases.facade.IVariaveis;

import java.util.ArrayList;

public class VariaveisImpl extends IdentificadorImpl implements IVariaveis  {

   private String tipoVariavel;
   private String modeloVariavel;
   private String StructPai; //Se for struct
   private int maxIndice;    //Se for array
   private int maxIndice2;   //Se for matriz
   private int indiceAtual;  //Se for array
   private int indiceAtual2; //Se for matriz
   private IdentificadorImpl identificador;

    public VariaveisImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
    }

    public String getTipoVariavel() {
        return tipoVariavel;
    }

    public void setTipoVariavel(String tipoVariavel) {
        this.tipoVariavel = tipoVariavel;
    }

    public String getStructPai() {
        return StructPai;
    }

    public void setStructPai(String structPai) {
        StructPai = structPai;
    }

    public int getMaxIndice() {
        return maxIndice;
    }

    public void setMaxIndice(int maxIndice) {
        this.maxIndice = maxIndice;
    }

    public int getMaxIndice2() {
        return maxIndice2;
    }

    public void setMaxIndice2(int maxIndice2) {
        this.maxIndice2 = maxIndice2;
    }

    public int getIndiceAtual() {
        return indiceAtual;
    }

    public void setIndiceAtual(int indiceAtual) {
        this.indiceAtual = indiceAtual;
    }

    public int getIndiceAtual2() {
        return indiceAtual2;
    }

    public void setIndiceAtual2(int indiceAtual2) {
        this.indiceAtual2 = indiceAtual2;
    }

    public IdentificadorImpl getIdentificador() {
        return identificador;
    }

    public void setIdentificador(IdentificadorImpl identificador) {
        this.identificador = identificador;
    }
}

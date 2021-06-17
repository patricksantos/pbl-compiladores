package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IVariaveis;

import java.util.ArrayList;
import java.util.HashMap;

public class VariaveisImpl extends IdentificadorImpl implements IVariaveis  {

   private String tipoVariavel;
   private String modeloVariavel;
   private boolean inicializado;
   private String structPai; //Se for struct
   private int maxIndice;    //Se for array
   private int maxIndice2;   //Se for matriz
   private int indiceAtual;  //Se for array
   private int indiceAtual2; //Se for matriz
   private String conteudo; // se for uma variavel normal
   private IdentificadorImpl identificador;
   private ArrayList<ElementosStruct> dadosStruct;
   //private ArrayList<String> atributosStruct;

    /*public VariaveisImpl(IdentificadorImpl identificador){
        this.identificador = identificador;
    }*/

    public VariaveisImpl(int id, Token token, int escopo){
        super(id,token,escopo);
        this.dadosStruct = new ArrayList<>();
        this.inicializado = false;
        this.structPai = "-";
    }

    public String getTipoVariavel() {
        return tipoVariavel;
    }

    public void setTipoVariavel(String tipoVariavel) {
        this.tipoVariavel = tipoVariavel;
    }

    public String getModeloVariavel() {
        return this.modeloVariavel;
    }

    public void setModeloVariavel(String modeloVariavel) {
        this.modeloVariavel = modeloVariavel;
    }

    public String getStructPai() {
        return structPai;
    }

    public void setStructPai(String structPai) {
        structPai = structPai;
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

    public ArrayList<ElementosStruct> getDadosStruct() {
        return this.dadosStruct;
    }

    public void setDadosStruct(ArrayList<ElementosStruct> dadosStruct) {
        for(ElementosStruct elemento: dadosStruct){
            this.dadosStruct.add(elemento);
        }
        //this.dadosStruct = dadosStruct;
    }

    public void setInicializado(boolean status){
        this.inicializado = status;
    }

    public boolean getInicializado(){
        return this.inicializado;
    }

}

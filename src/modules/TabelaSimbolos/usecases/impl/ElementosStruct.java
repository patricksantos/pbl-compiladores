package modules.TabelaSimbolos.usecases.impl;

public class ElementosStruct {

    private String nome;
    private String tipo;
    private boolean inicializado;

    public ElementosStruct(String nome, String tipo, boolean inicializado){
        this.nome = nome;
        this.tipo = tipo;
        this.inicializado = inicializado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean getInicializado() {
        return inicializado;
    }

    public void setInicializado(boolean inicializado) {
        this.inicializado = inicializado;
    }

}

package domain.error;

public class Erro implements IError {

    private String tipo;
    private String lexema;
    private String linha;

    public Erro(String tipo, String lexema){
        this.lexema = lexema;
        this.tipo = tipo;
    }

    @Override
    public String info() {
        return null;
    }
}

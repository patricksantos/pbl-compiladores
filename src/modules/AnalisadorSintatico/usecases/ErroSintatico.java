package modules.AnalisadorSintatico.usecases;

import domain.error.IError;

public class ErroSintatico implements IError {

    public String linha;
    public String lexemaEsperado;
    public String lexemaObtido;

    public ErroSintatico(String linha, String lexemaEsperado, String lexemaObtido){
        super();
        this.linha = linha;
        this.lexemaEsperado = lexemaEsperado;
        this.lexemaObtido = lexemaObtido;
    }

    public String getLinhaSintatico(){
        return this.linha;
    }

    public String getLexemaEsperado(){
        return this.lexemaEsperado;
    }

    public String getLexemaObtido(){
        return this.lexemaObtido;
    }

    @Override
    public String info(){
        return this.getLinhaSintatico() + " " + this.getLexemaObtido() + " Esperava " + this.getLexemaEsperado();
    }
}

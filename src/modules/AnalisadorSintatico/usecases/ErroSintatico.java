package modules.AnalisadorSintatico.usecases;

import java.util.ArrayList;

public class ErroSintatico {

    public String linha;
    public String lexemaEsperado;
    public String lexemaObtido;

    public ErroSintatico(String linha, String lexemaEsperado, String lexemaObtido){
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

    public String info(){
        return this.getLinhaSintatico() + " " + this.getLexemaObtido() + " Esperava " + this.getLexemaEsperado();
    }
}

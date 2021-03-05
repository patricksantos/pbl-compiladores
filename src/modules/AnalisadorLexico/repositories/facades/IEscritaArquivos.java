package modules.AnalisadorLexico.repositories.facades;

import modules.AnalisadorLexico.entities.Token;

import java.io.IOException;
import java.util.ArrayList;

public interface IEscritaArquivos {
    public void escrita(String nome,String texto) throws IOException;

    public void escrita(ArrayList<Token> tokens, String nome)throws IOException;
}

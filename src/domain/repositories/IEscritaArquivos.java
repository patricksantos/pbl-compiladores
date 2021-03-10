package domain.repositories;

import domain.entities.Token;

import java.io.IOException;
import java.util.ArrayList;

public interface IEscritaArquivos {
    public void escrita(String nome,String texto) throws IOException;

    public void escrita(ArrayList<Token> tokens,ArrayList<Token> erros,String nome)throws IOException;
}

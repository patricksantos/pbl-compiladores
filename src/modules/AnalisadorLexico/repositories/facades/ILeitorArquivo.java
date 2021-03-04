package modules.AnalisadorLexico.repositories.facades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface ILeitorArquivo {
    public ArrayList<String> leituraArquivo(String nomeArquivo) throws IOException;

    public int getNumeroArquivos();

    public Set<String> getNomesArquivos();
}

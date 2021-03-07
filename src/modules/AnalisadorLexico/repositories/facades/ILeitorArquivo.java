package modules.AnalisadorLexico.repositories.facades;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface ILeitorArquivo {
    ArrayList<String> leituraArquivo(String nomeArquivo) throws IOException;

    int getNumeroArquivos();

    Set<String> getNomesArquivos();
}

package modules.AnalisadorLexico.repositories.facades;

import java.io.IOException;

public interface IEscritaArquivos {
    public void escrita(String nome,String texto) throws IOException;
}

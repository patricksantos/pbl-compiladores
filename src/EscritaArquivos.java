import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe responsável por escrever os tokens nos respectivos arquivos de saída;
 * */
public class EscritaArquivos {

    private ArrayList<Token> tokens;
    private String diretorio;

    /**
     * @param tokens uma lista de tokens que foram identificados em 1 arquivo
     * */
    public EscritaArquivos(ArrayList<Token> tokens){
        this.tokens = tokens;
        diretorio = "." + File.pathSeparator + "output";
    }

    public void escrita() throws IOException {
        BufferedWriter escrita = new BufferedWriter(new FileWriter(this.diretorio));
        for(Token token: this.tokens){
            escrita.write(token.info());
        }
    }

}

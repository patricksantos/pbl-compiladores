import java.io.*;
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
        diretorio = "./output";
    }

    public EscritaArquivos(){
        diretorio = "./output";
    }

    public void escrita(String nome,String teste) throws IOException {

        int auxiliar = nome.lastIndexOf("a");
        String arquivoSaida = "Saida" + nome.substring(auxiliar + 1);

        //BufferedWriter escritas = new BufferedWriter(new FileWriter(this.diretorio + "/" + arquivoSaida));
        FileOutputStream arq = new FileOutputStream(this.diretorio + "/" + arquivoSaida);
        DataOutputStream escrever = new DataOutputStream(arq);
        teste = teste.trim();

        for(Token token: this.tokens){
            escrever.writeChars(token.info());
        }
    }

}

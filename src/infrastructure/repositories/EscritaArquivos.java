package infrastructure.repositories;

import domain.entities.Token;
import domain.repositories.IEscritaArquivos;

import java.io.*;
import java.util.ArrayList;

/**
 * Classe responsável por escrever os resultTokens nos respectivos arquivos de saída;
 * */
public class EscritaArquivos implements IEscritaArquivos {

    private ArrayList<Token> tokens;
    private String diretorio;

    /**
     * @param tokens uma lista de resultTokens que foram identificados em 1 arquivo
     * */
    public EscritaArquivos(ArrayList<Token> tokens){
        this.tokens = tokens;
        diretorio = "./output";
    }

    public EscritaArquivos(){
        diretorio = "./output";
    }

    @Override
    public void escrita(String nome, String texto) throws IOException {
        String arquivoSaida = nome.replace("entrada", "saida");

        FileOutputStream arquivo = new FileOutputStream(this.diretorio + "/" + arquivoSaida);
        DataOutputStream escrever = new DataOutputStream(arquivo);

        escrever.writeUTF(texto.trim());

        escrever.flush();
        escrever.close();
    }

    @Override
    public void escrita(ArrayList<Token> tokens, String nomeArquivo) throws IOException {
        //String arquivoSaida = nomeArquivo.replace("entrada", "saida");

        int auxiliar = nomeArquivo.lastIndexOf("a");
        String arquivoSaida = "Saida" + nomeArquivo.substring(auxiliar + 1);

        FileOutputStream arquivo = new FileOutputStream(this.diretorio + "/" + arquivoSaida);
        DataOutputStream escrever = new DataOutputStream(arquivo);

        for(Token token : tokens){
            escrever.writeBytes(token.info());
            escrever.writeChars("\n");
        }

        escrever.flush();
        escrever.close();
    }

}

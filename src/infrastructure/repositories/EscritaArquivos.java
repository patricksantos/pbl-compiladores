package infrastructure.repositories;

import domain.entities.Token;
import domain.repositories.IEscritaArquivos;

import java.io.*;
import java.util.ArrayList;

/**
 * Classe responsável por escrever os tokens identificados nos respectivos arquivos de saída;
 * */
public class EscritaArquivos implements IEscritaArquivos {

    private ArrayList<Token> tokens;//Lista de tokens que irão ser escritos no arquivo de saida
    private String diretorio; //diretorio onde irá ficar os arquivos de saida

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

    @Override
    public void escrita(String nome, String texto) throws IOException {
        String arquivoSaida = nome.replace("entrada", "saida");

        FileOutputStream arquivo = new FileOutputStream(this.diretorio + "/" + arquivoSaida);
        DataOutputStream escrever = new DataOutputStream(arquivo);

        escrever.writeUTF(texto.trim());

        escrever.flush();
        escrever.close();
    }
    /**
     * Método que escreve as informações no arquivo de saida
     * @param tokens uma lista de tokens que foram identificados em um arquivo
     * @param erros os possiveis erros que ocorreram no arquivo
     * @param nomeArquivo nome do arquivo de onde os tokens e os possiveis erro foram retirados.
     * */
    @Override
    public void escrita(ArrayList<Token> tokens, ArrayList<Token> erros,String nomeArquivo) throws IOException {
        //String arquivoSaida = nomeArquivo.replace("entrada", "saida");

        //Cria o nome do arquivo de saida a partir do nome do arquivo de entrada.
        int auxiliar = nomeArquivo.lastIndexOf("a");
        String arquivoSaida = "Saida" + nomeArquivo.substring(auxiliar + 1);

        FileOutputStream arquivo = new FileOutputStream(this.diretorio + "/" + arquivoSaida);
        DataOutputStream escrever = new DataOutputStream(arquivo);

        //Escreve os tokens no arquivo de saida
        for(Token token : tokens){
            escrever.writeBytes(token.info());
            escrever.writeChars("\n");
        }

        escrever.writeChars("\n");

        //Caso tenha, escreve os erros no arquivo de saida
        for(Token token : erros){
            escrever.writeBytes(token.info());
            escrever.writeChars("\n");
        }
        escrever.flush();
        escrever.close();
    }

}

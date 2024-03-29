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
    private boolean erroSintatico;
    /**
     * @param tokens uma lista de tokens que foram identificados em 1 arquivo
     * */
    public EscritaArquivos(ArrayList<Token> tokens){
        this.tokens = tokens;
        diretorio = "." + File.separator + "output";
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
    public void escrita(ArrayList<Token> tokens, ArrayList<Token> erros,ArrayList<String> errosSemanticos,String nomeArquivo) throws IOException {

        //Cria o nome do arquivo de saida a partir do nome do arquivo de entrada.
        int auxiliar = nomeArquivo.lastIndexOf("a");
        String arquivoSaida = "Saida" + nomeArquivo.substring(auxiliar + 1);
        this.erroSintatico = false;
        FileOutputStream arquivo = new FileOutputStream(this.diretorio + File.separator + arquivoSaida);
        DataOutputStream escrever = new DataOutputStream(arquivo);

        //Escreve os tokens no arquivo de saida
        for(Token token : tokens){
            if(token.getErroSintatico()){
                this.erroSintatico = true;
            }
            escrever.writeBytes(token.info());
            escrever.writeChars("\n");
        }

        escrever.writeChars("\n");
        //Mensagem de sucesso caso não tenha nenhum erro no arquivo de entrada que foi lido
        if(erros.size() == 0 && (!erroSintatico) && errosSemanticos.size() == 0){
            escrever.writeBytes("Análise feita com sucesso, nenhum erro foi encontrado.");
        }
        //Caso tenha, escreve os erros no arquivo de saida
        for(Token token : erros){
            escrever.writeBytes(token.info());
            escrever.writeChars("\n");
        }
        if(errosSemanticos.size() > 0){
            escrever.writeBytes("Erros Semânticos:");
            escrever.writeChars("\n");
            for(String erro: errosSemanticos){
                escrever.writeBytes(erro);
                escrever.writeChars("\n");
            }
        }
        escrever.flush();
        escrever.close();
    }

}

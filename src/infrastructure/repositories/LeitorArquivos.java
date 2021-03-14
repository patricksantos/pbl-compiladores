package infrastructure.repositories;

import domain.repositories.ILeitorArquivo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Classe responsável por ler os arquivos de entrada.
* */
public class LeitorArquivos implements ILeitorArquivo {

    private HashMap<String,File> lista_arquivos;//Arquivos encontrados na pasta input
    private File diretorio;//diretorio onde os arquivos foram salvos
    private int  numeroArquivos;//quantidade de arquivos encontrados
    private int arquivoAtual;//posição do arquivo que será lido

    public LeitorArquivos(){
        this.diretorio = new File("./input");
        this.lista_arquivos = new HashMap<String,File>();
        //Salva os arquivos encontrados na pasta input
        File[] arquivos = diretorio.listFiles();
        //Os nome do arquivo é utilizado para recuperar o arquivo.
        for(File arquivo: arquivos){
            lista_arquivos.put(arquivo.getName(),arquivo);
        }
        //Salva a quantidade de arquivos que foram lidos
        this.numeroArquivos = lista_arquivos.size();
    }
    /**
     * Método responsável por ler os dados contidos em um arquivo.
     * @param nomeArquivo nome do arquivo que será lido
     * @return uma lista, cada linha do arquivo é guardada em uma posição da lista
     * */
    @Override
    public ArrayList<String> leituraArquivo(String nomeArquivo) throws IOException {

        ArrayList<String> linhas = new ArrayList<>();
        //Recupera o arquivo na lista de arquivos a partir do nome
        File arquivo = this.lista_arquivos.get(nomeArquivo);
        BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
        String linha = leitor.readLine();
        //Ler todas as linhas contidas no arquivo
        while (linha != null){
            linhas.add(linha);
            linha = leitor.readLine();
        }
        return linhas;
    }

    /**
     * @return a quantidade de arquivos na pasta
     * */
    @Override
    public int getNumeroArquivos(){
        return this.numeroArquivos;
    }

    /**
     * @return os nomes dos arquivos que foram encontrados na pasta input
     * */
    @Override
    public Set<String> getNomesArquivos(){
        return  this.lista_arquivos.keySet();
    }
}

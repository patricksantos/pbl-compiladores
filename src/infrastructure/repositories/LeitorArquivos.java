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
        File[] arquivos = diretorio.listFiles();
        for(File arquivo: arquivos){
            lista_arquivos.put(arquivo.getName(),arquivo);
        }
        this.numeroArquivos = lista_arquivos.size();
    }
    /**
     * Método responsável por ler os dados contidos em um arquivo.
     * */
    @Override
    public ArrayList<String> leituraArquivo(String nomeArquivo) throws IOException {

        ArrayList<String> linhas = new ArrayList<>();
        File arquivo = this.lista_arquivos.get(nomeArquivo);
        BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
        String linha = leitor.readLine();
        //Ler todas as linhas contidas no arquivo
        while (linha != null){
            linhas.add(linha);
            linha = leitor.readLine();

        }
        //O arquivo já foi lido então, quando a função for chamanda novamente o proximo arquivo será lido.
        //arquivoAtual++;

        return linhas;
    }

    /**
     * @return a quantidade de arquivos na pasta
     * */
    @Override
    public int getNumeroArquivos(){
        return this.numeroArquivos;
    }

    @Override
    public Set<String> getNomesArquivos(){
        return  this.lista_arquivos.keySet();
    }
}

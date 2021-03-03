import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Classe responsável por ler os arquivos de entrada.
* */
public class LeitorArquivos {

    private File lista_arquivos[];
    private File diretorio;
    private int  numeroArquivos;

    public LeitorArquivos(){
        this.diretorio = new File("./input");
        this.lista_arquivos = diretorio.listFiles();
        this.numeroArquivos = lista_arquivos.length;
    }
    /**
     * Método responsável por ler os dados contidos nos arquivos.
     * */
    public void leitura() throws FileNotFoundException {
        //
        for(File arquivo:lista_arquivos){
            BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
        }
    }
}

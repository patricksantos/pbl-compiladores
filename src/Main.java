import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        //DelimitadorComentario retorno = new DelimitadorComentario();
        //String texto = "teste 1 teste 2 /* tessste*eeesk/fadf*ads */ testeteste 1 teste 2 teste 3  teste 1 teste 2 teste 3 // oiiiii \\n afdsfsaf";
        //String texto2 = "// oii*/*iii \\n teste 1 teste 2 /* tesssteeeeskfadfads */ testeteste 1 teste 2 teste 3  teste 1 teste 2 teste 3  afdsfsaf";
        //System.out.println(retorno.getLexema(texto, 0));
        //System.out.println(retorno.getLexema(texto2, 0));

        LeitorArquivos leitor = new LeitorArquivos();
        EscritaArquivos escrita = new EscritaArquivos();
        Set<String> nomesArquivos = leitor.getNomesArquivos();
        for(String arquivo: nomesArquivos){
            ArrayList<String> conteudo;
            conteudo = leitor.leituraArquivo(arquivo);
            System.out.println("oi");
            escrita.escrita(arquivo,conteudo.get(0));
        }
    }
}

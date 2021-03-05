import modules.AnalisadorLexico.ControllerAnalisadorLexico;
import modules.AnalisadorLexico.repositories.facades.IEscritaArquivos;
import modules.AnalisadorLexico.repositories.facades.ILeitorArquivo;
import modules.AnalisadorLexico.repositories.impl.EscritaArquivos;
import modules.AnalisadorLexico.repositories.impl.LeitorArquivos;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;
import modules.AnalisadorLexico.usecases.impl.AnalisadorLexicoImpl;
import modules.AnalisadorLexico.usecases.impl.automatos.DelimitadorComentarioImpl;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {
        //old.DelimitadorComentario retorno = new old.DelimitadorComentario();
        //String texto = "teste 1 teste 2 /* tessste*eeesk/fadf*ads */ testeteste 1 teste 2 teste 3  teste 1 teste 2 teste 3 // oiiiii \\n afdsfsaf";
        //String texto2 = "// oii*/*iii \\n teste 1 teste 2 /* tesssteeeeskfadfads */ testeteste 1 teste 2 teste 3  teste 1 teste 2 teste 3  afdsfsaf";
        //System.out.println(retorno.getLexema(texto, 0));
        //System.out.println(retorno.getLexema(texto2, 0));

//        LeitorArquivos leitor = new LeitorArquivos();
//        EscritaArquivos escrita = new EscritaArquivos();
//        Set<String> nomesArquivos = leitor.getNomesArquivos();
//        for(String arquivo: nomesArquivos){
//            ArrayList<String> conteudo;
//            conteudo = leitor.leituraArquivo(arquivo);
//            System.out.println("oi");
//            escrita.escrita(arquivo,conteudo.get(0));
//        }

        IAutomato delimitadorComentario = new DelimitadorComentarioImpl();

        ILeitorArquivo leitorArquivo = new LeitorArquivos();
        IEscritaArquivos escritaArquivos = new EscritaArquivos();
        IAnalisadorLexico analisadorLexico = new AnalisadorLexicoImpl(delimitadorComentario);

        ControllerAnalisadorLexico controllerAnalisadorLexico = new ControllerAnalisadorLexico(leitorArquivo, escritaArquivos, analisadorLexico);
        controllerAnalisadorLexico.iniciarLeitura();
        //controllerAnalisadorLexico.escreverToken();

    }
}

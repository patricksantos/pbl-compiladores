package modules;

import domain.entities.Token;
import domain.repositories.IEscritaArquivos;
import domain.repositories.ILeitorArquivo;
import infrastructure.repositories.EscritaArquivos;
import infrastructure.repositories.LeitorArquivos;
import modules.AnalisadorLexico.ControllerAnalisadorLexico;
import modules.AnalisadorSintatico.ControllerAnalisadorSintatico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Controller {

    private ControllerAnalisadorLexico controllerAnalisadorLexico;
    private ControllerAnalisadorSintatico controllerAnalisadorSintatico;
    private final ILeitorArquivo leitorArquivo;
    private final IEscritaArquivos escritaArquivos;

    public Controller(){
        this.controllerAnalisadorLexico = new ControllerAnalisadorLexico();
        this.controllerAnalisadorSintatico = new ControllerAnalisadorSintatico();
        //Quando o leitorArquivo é instanciado o mesmo ler os arquivos que estão na pasta input.
        this.leitorArquivo = new LeitorArquivos();
        this.escritaArquivos = new EscritaArquivos();
    }

    public void iniciarLeitura() throws IOException {
        this.analisarArquivos();
    }

    /**
     * Método que seleciona um arquivo para ser analisado pelo analisador léxico, e retornar os tokens encontrados.
     * Assim que termina a analise de um arquivo ele passa para o próximo, até ler todos os arquivos.
     * */
    private void analisarArquivos() throws IOException {
        //Retorna os nomes dos araquivos da pasta input
        Set<String> nomesArquivos = leitorArquivo.getNomesArquivos();
        if(nomesArquivos != null) {
            for (String arquivo : nomesArquivos) {
                //Retorna o conteudo do arquivo que está sendo lido atualmente.
                ArrayList<String> conteudo = this.leitorArquivo.leituraArquivo(arquivo);
                //Retorna os tokens identificados no arquivo.
                ArrayList<Token> tokens = controllerAnalisadorLexico.analiseArquivo(conteudo);
                //Escreve os tokens e os erros(caso ocorram), no arquivo de saida.
                tokens = this.controllerAnalisadorSintatico.iniciarLeitura(tokens);
                this.escritaArquivos.escrita(tokens, controllerAnalisadorLexico.getErros(), arquivo);
            }
        }
    }
}

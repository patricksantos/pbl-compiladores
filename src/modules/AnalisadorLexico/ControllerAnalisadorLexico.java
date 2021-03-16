package modules.AnalisadorLexico;

import domain.entities.Token;
import domain.repositories.IEscritaArquivos;
import domain.repositories.ILeitorArquivo;
import infrastructure.repositories.EscritaArquivos;
import infrastructure.repositories.LeitorArquivos;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.*;
import modules.AnalisadorLexico.usecases.impl.AnalisadorLexicoImpl;
import modules.AnalisadorLexico.usecases.impl.automatos.*;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;
import modules.TabelaSimbolos.usecases.impl.TabelaSimbolosImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ControllerAnalisadorLexico {
    private final ILeitorArquivo leitorArquivo;
    private final IEscritaArquivos escritaArquivos;
    private final IAnalisadorLexico analisadorLexico;

    public ControllerAnalisadorLexico() {
        ITabelaSimbolos tabelaSimbolos = new TabelaSimbolosImpl();
        //Interfaces dos automatos
        IDelimitadorComentario delimitadorComentario = new DelimitadorComentarioImpl();
        IOperadoresLogicos operadoresLogicos = new OperadoresLogicosImpl();
        IOperadoresAritmeticos operadoresAritmeticos = new OperadoresAritmeticosImpl();
        IDelimitadores delimitadores = new DelimitadoresImpl();
        IPalavrasReservadasIdentificadores palavrasReservadasIdentificadores = new PalavrasReservadasIdentificadoresImpl(tabelaSimbolos);
        IOperadoresRelacionais operadoresRelacionais = new OperadoresRelacionaisImpl();
        ICadeiasCaracteres cadeiasCaracteres = new CadeiasCaracteresImpl();
        ISimbolo simbolos = new SimbolosImpl();
        INumero numeros = new NumeroImpl();

        this.analisadorLexico = new AnalisadorLexicoImpl(tabelaSimbolos, delimitadorComentario, operadoresLogicos, operadoresAritmeticos,
                delimitadores, palavrasReservadasIdentificadores, operadoresRelacionais, cadeiasCaracteres, simbolos, numeros);

        //Quando o leitorArquivo é instanciado o mesmo ler os arquivos que estão na pasta input.
        this.leitorArquivo = new LeitorArquivos();
        this.escritaArquivos = new EscritaArquivos();
    }

    /**
     * Método que inicia a leitura dos arquivos da pasta input
     * */
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
                ArrayList<Token> tokens = analisadorLexico.analiseArquivo(conteudo);
                //Escreve os tokens e os erros(caso ocorram), no arquivo de saida.
                this.escritaArquivos.escrita(tokens, analisadorLexico.getErros(), arquivo);
            }
        }
    }

    private void escreverArquivo(String arquivo, String conteudo) throws IOException {
        escritaArquivos.escrita(arquivo, conteudo);
    }

    private void escreverToken() {

        try{
            Set<String> nomesArquivos = leitorArquivo.getNomesArquivos();

            for(String arquivo: nomesArquivos){
                ArrayList<String> conteudo = this.leitorArquivo.leituraArquivo(arquivo);
                String token = analisadorLexico.analise(conteudo.get(0), 0).info();
                //System.out.println(token);
                this.escreverArquivo(arquivo, token);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}

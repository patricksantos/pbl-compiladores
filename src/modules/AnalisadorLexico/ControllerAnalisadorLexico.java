package modules.AnalisadorLexico;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.repositories.facades.IEscritaArquivos;
import modules.AnalisadorLexico.repositories.facades.ILeitorArquivo;
import modules.AnalisadorLexico.repositories.impl.EscritaArquivos;
import modules.AnalisadorLexico.repositories.impl.LeitorArquivos;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.automatos.*;
import modules.AnalisadorLexico.usecases.impl.AnalisadorLexicoImpl;
import modules.AnalisadorLexico.usecases.impl.automatos.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ControllerAnalisadorLexico {
    private final ILeitorArquivo leitorArquivo;
    private final IEscritaArquivos escritaArquivos;
    private final IAnalisadorLexico analisadorLexico;

    public ControllerAnalisadorLexico() {
        IDelimitadorComentario delimitadorComentario = new DelimitadorComentarioImpl();
        IOperadoresLogicos operadoresLogicos = new OperadoresLogicosImpl();
        IOperadoresAritmeticos operadoresAritmeticos = new OperadoresAritmeticosImpl();
        IDelimitadores delimitadores = new DelimitadoresImpl();
        IPalavrasReservadasIdentificadores palavrasReservadasIdentificadores = new PalavrasReservadasIdentificadoresImpl();

        this.analisadorLexico = new AnalisadorLexicoImpl(delimitadorComentario, operadoresLogicos, operadoresAritmeticos, delimitadores, palavrasReservadasIdentificadores);

        this.leitorArquivo = new LeitorArquivos();
        this.escritaArquivos = new EscritaArquivos();
    }

    public void iniciarLeitura() throws IOException {
        this.analisarArquivos();
    }

    private void analisarArquivos() throws IOException {

        Set<String> nomesArquivos = leitorArquivo.getNomesArquivos();
        for(String arquivo: nomesArquivos){
            ArrayList<String> conteudo = this.leitorArquivo.leituraArquivo(arquivo);
            ArrayList<Token> tokens = analisadorLexico.analiseArquivo(conteudo);
            this.escritaArquivos.escrita(tokens, arquivo);
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
                System.out.println(token);
                this.escreverArquivo(arquivo, token);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}

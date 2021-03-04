package modules.AnalisadorLexico;

import modules.AnalisadorLexico.repositories.facades.IEscritaArquivos;
import modules.AnalisadorLexico.repositories.facades.ILeitorArquivo;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ControllerAnalisadorLexico {
    private final ILeitorArquivo leitorArquivo;
    private final IEscritaArquivos escritaArquivos;
    private final IAnalisadorLexico analisadorLexico;

    public ControllerAnalisadorLexico(ILeitorArquivo leitorArquivo, IEscritaArquivos escritaArquivos, IAnalisadorLexico analisadorLexico) throws IOException {
        this.leitorArquivo = leitorArquivo;
        this.escritaArquivos = escritaArquivos;
        this.analisadorLexico = analisadorLexico;
    }

    public void escreverArquivo(String arquivo, String conteudo) throws IOException {
        escritaArquivos.escrita(arquivo, conteudo);
    }

    public void escreverToken() throws IOException {

        try{
            Set<String> nomesArquivos = leitorArquivo.getNomesArquivos();

            for(String arquivo: nomesArquivos){
                ArrayList<String> conteudo = this.leitorArquivo.leituraArquivo(arquivo);
                String token = analisadorLexico.analise(conteudo.get(0), 0).info();
                System.out.println(token);
                this.escreverArquivo(arquivo, token);
            }
        }catch (Exception e){

        }
    }


}

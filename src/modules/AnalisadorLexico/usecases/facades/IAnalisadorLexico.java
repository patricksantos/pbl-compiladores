package modules.AnalisadorLexico.usecases.facades;

import modules.AnalisadorLexico.entities.Token;

import java.util.ArrayList;

public interface IAnalisadorLexico {
    public Token analise(String texto, int posicao);

    public String proximaLinha();

    public void adicionarToken(Token token);

    public void setPosicao(int posicao);

    public ArrayList<Token> analiseArquivo(ArrayList<String> conteudoArquivo);
}
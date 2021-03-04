package modules.AnalisadorLexico.usecases.facades;

import modules.AnalisadorLexico.entities.Token;

public interface IAnalisadorLexico {
    public Token analise(String texto, int posicao);
}

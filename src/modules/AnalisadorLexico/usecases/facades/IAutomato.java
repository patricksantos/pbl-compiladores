package modules.AnalisadorLexico.usecases.facades;

import modules.AnalisadorLexico.entities.Token;

public interface IAutomato {

    Token getToken(String texto, int posicao);

    void getError();

    int getPosicaoFinal();
}

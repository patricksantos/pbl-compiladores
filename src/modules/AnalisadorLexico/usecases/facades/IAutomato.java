package modules.AnalisadorLexico.usecases.facades;

import domain.entities.Token;

public interface IAutomato {

    Token getToken(String texto, int posicao);

    void getError();

    int getPosicaoFinal();
}

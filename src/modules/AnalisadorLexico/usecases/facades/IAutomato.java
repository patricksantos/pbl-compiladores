package modules.AnalisadorLexico.usecases.facades;

import domain.entities.Token;

public interface IAutomato {

    Token getToken(String texto, int posicao);

    int getPosicaoFinal();

    void setPosicaoFinal(int posicaoFinal);
}

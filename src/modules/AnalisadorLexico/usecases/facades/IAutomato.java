package modules.AnalisadorLexico.usecases.facades;

import modules.AnalisadorLexico.entities.Token;

public interface IAutomato {

    public Token getToken(String texto, int posicao);

    public void getError();

}

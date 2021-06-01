package modules.TabelaSimbolos.usecases.facade;

import domain.entities.Token;

public interface IIdentificador {

    void setId(int id);
    int getId();
    void setToken(Token token);
    Token getToken();
    void setEscopo(int escopo);
    int getEscopo();
    void configurarIdentificador(int id, Token token, int escopo);
}

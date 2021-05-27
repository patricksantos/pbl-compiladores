package modules.TabelaSimbolos.usecases.facade;

import domain.entities.Token;

import java.util.ArrayList;
import java.util.Map;

public interface ITabelaSimbolos {

    ArrayList<Token> getTokensTabela();

    boolean isPalavraReservada(String lexema);

   // void setSimbolo(String chave, String valor);

    void setSimbolo(int chave, IIdentificador valor);

    boolean adicionarSimbolo(int id, IIdentificador identificador);

    IIdentificador getSimbolo(Token token);
}

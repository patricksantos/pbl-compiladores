package modules.TabelaSimbolos.usecases.facade;

import domain.entities.Token;

import java.util.ArrayList;
import java.util.Map;

public interface ITabelaSimbolos {

    ArrayList<Token> getTokensTabela();

    boolean isPalavraReservada(String texto);

    void setSimbolo(String chave, String valor);
}

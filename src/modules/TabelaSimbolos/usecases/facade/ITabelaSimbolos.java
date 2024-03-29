package modules.TabelaSimbolos.usecases.facade;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.impl.IdentificadorImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface ITabelaSimbolos {

    ArrayList<Token> getTokensTabela();

    boolean isPalavraReservada(String lexema);

   // void setSimbolo(String chave, String valor);

    void setSimbolo(int chave, IdentificadorImpl valor);

    void setSimbolo(String chave, String valor);

    boolean adicionarSimbolo(int id, IdentificadorImpl identificador);

    IIdentificador getSimbolo(Token token, String tipo);

    IIdentificador getSimboloL(Token token, String tipo);

    public ArrayList<IIdentificador> getSimbolos(Token token,String tipo);

    public HashMap<Integer, IdentificadorImpl> getTabelaSimbolos();

    int numeroSimbolos();
}

package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TabelaSimbolosImpl implements ITabelaSimbolos {

    HashMap<String, String> tabelaSimbolos;

    public TabelaSimbolosImpl() {
        this.tabelaSimbolos =  new HashMap<String, String>();
        this.inserirPalavrasReservadas();
    }

    @Override
    public ArrayList<Token> getTokensTabela() {
        ArrayList<Token> tokens = new ArrayList<Token>();

        for(Map.Entry<String, String> map : this.tabelaSimbolos.entrySet()){
            if( this.isPalavraReservada(map.getValue()) )
            {
                Token token = new Token(map.getValue(), "",false);
                tokens.add(token);
            }
            else
            {
                Token token = new Token(map.getValue(), map.getKey(),false);
                tokens.add(token);
            }
        }

        return tokens;
    }

    @Override
    public boolean isPalavraReservada(String texto) {
        return this.tabelaSimbolos.containsKey(texto);
    }

    @Override
    public void setSimbolo(String chave, String valor) {

        this.tabelaSimbolos.put(chave, valor);

//        if( this.tabelaSimbolos.containsKey(chave) ){
//            return this.tabelaSimbolos.keySet(chave);
//        }
//
//        return null;
    }

    private void inserirPalavrasReservadas(){
        ArrayList<String> regexPalavraReservada = new ArrayList<String>(Arrays.asList("var", "const", "typedef", "struct", "extends", "procedure", "function", "start", "return", "if", "else", "then", "while", "read", "print", "int", "real", "boolean", "true", "string", "false", "global", "local"));

        for(String palavra : regexPalavraReservada){
            this.tabelaSimbolos.put(palavra, palavra);
        }

    }
}

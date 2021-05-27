package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IIdentificador;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;

import java.util.*;

public class TabelaSimbolosImpl implements ITabelaSimbolos {

    //HashMap<String, String> tabelaSimbolos;
    HashMap<Integer, IIdentificador> tabelaSimbolos;

    public TabelaSimbolosImpl() { // Construtor
        //this.tabelaSimbolos =  new HashMap<String, String>();
        this.tabelaSimbolos =  new HashMap<Integer, IIdentificador>();
        this.inserirPalavrasReservadas(); // Insere todas as palavras reservadas dentro da tabela
    }

     /**
     * @return todos os Tokens dentro da tabela
     * */
    @Override
    /*public ArrayList<Token> getTokensTabela() {
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
    }*/
    public ArrayList<Token> getTokensTabela() {
        ArrayList<Token> tokens = new ArrayList<Token>();

        for(Map.Entry<Integer, IIdentificador> map : this.tabelaSimbolos.entrySet()){
            if( this.isPalavraReservada(map.getValue().getToken().getLexema()) )
            {
                Token token = new Token("PRE", map.getValue().getToken().getLexema(),false);
                tokens.add(token);
            }
            else
            {
                Token token = new Token("IDE" ,map.getValue().getToken().getLexema(),false);
                tokens.add(token);
            }
        }

        return tokens;
    }

     /**
     * @return se o texto é uma palavra reservada
     * */
    @Override
    /*public boolean isPalavraReservada(String texto) {
        return this.tabelaSimbolos.containsKey(texto) && this.tabelaSimbolos.containsValue(texto);
    }*/

    public boolean isPalavraReservada(String lexema) {
        Collection<IIdentificador> aux = tabelaSimbolos.values();
        boolean controle = false;
        for(IIdentificador identificador: aux){
            if(identificador.getToken().getLexema().equals(lexema)){
                controle = true;
                break;
            }else{
                controle = false;
            }
        }
        return controle;
    }

     /**
     * @param chave a chave do simbolo
     * @param valor o valor do simbolo
     * */
    @Override
    /*public void setSimbolo(String chave, String valor) {

        this.tabelaSimbolos.put(chave, valor);

//        if( this.tabelaSimbolos.containsKey(chave) ){
//            return this.tabelaSimbolos.keySet(chave);
//        }
//
//        return null;
    }*/
    public void setSimbolo(int chave, IIdentificador valor) {

        this.tabelaSimbolos.put(chave, valor);
    }
     /**
     * Ele insere todos as palavras reservador quando a Tabela de Simbolos for instanciada
     * */
    private void inserirPalavrasReservadas(){
        ArrayList<String> regexPalavraReservada = new ArrayList<String>(Arrays.asList("var", "const", "typedef", "struct", "extends", "procedure", "function", "start", "return", "if", "else", "then", "while", "read", "print", "int", "real", "boolean", "true", "string", "false", "global", "local"));
        int controle = -1;
        for(String reservada: regexPalavraReservada){
            Token token = new Token("",reservada,false);
            IdentificadorImpl identificador = new IdentificadorImpl();
            identificador.setToken(token);
            identificador.setId(controle);
            this.tabelaSimbolos.put(controle, identificador);
            controle--;
        }
        /*for(String palavra : regexPalavraReservada){
            this.tabelaSimbolos.put(palavra, palavra);
        }*/

    }
}

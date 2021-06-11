package modules.TabelaSimbolos.usecases.impl;

import domain.entities.Token;
import modules.TabelaSimbolos.usecases.facade.IIdentificador;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;

import java.util.*;

public class TabelaSimbolosImpl implements ITabelaSimbolos {

    HashMap<String, String> tabelaSimbolos1;
    HashMap<Integer, IdentificadorImpl> tabelaSimbolos;
    int a = 1;
    public TabelaSimbolosImpl() { // Construtor
        this.tabelaSimbolos1 =  new HashMap<String, String>();
        this.tabelaSimbolos =  new HashMap<Integer, IdentificadorImpl>();
        //this.inserirPalavrasReservadas(); // Insere todas as palavras reservadas dentro da tabela
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
        for(Map.Entry<Integer, IdentificadorImpl> map : this.tabelaSimbolos.entrySet()){
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
        Collection<IdentificadorImpl> aux = tabelaSimbolos.values();
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
    public void setSimbolo(String chave, String valor) {

        this.tabelaSimbolos1.put(chave, valor);

//        if( this.tabelaSimbolos.containsKey(chave) ){
//            return this.tabelaSimbolos.keySet(chave);
//        }
//
//        return null;
    }
    public void setSimbolo(int chave, IdentificadorImpl valor) {

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
            IdentificadorImpl identificador = new IdentificadorImpl(controle,token,1);
            /*identificador.setToken(token);
            identificador.setId(controle);*/
            this.tabelaSimbolos.put(controle, identificador);
            controle--;
        }
        /*for(String palavra : regexPalavraReservada){
            this.tabelaSimbolos.put(palavra, palavra);
        }*/

    }

    public boolean adicionarSimbolo(int id, IdentificadorImpl identificador){

        if(this.tabelaSimbolos.containsKey(id)){
            return false;
        }else{
            this.tabelaSimbolos.put(id,identificador);
            return true;
        }
    }

    public IIdentificador getSimbolo(Token token,String tipo){
        if(this.tabelaSimbolos.isEmpty()){
            return null;
        }
        IIdentificador aux = null;
        for(IIdentificador simbolo: this.tabelaSimbolos.values()){
            switch (tipo){
                case "variavel":
                    if(simbolo instanceof VariaveisImpl){
                        if(simbolo.getToken() == token){
                            aux = simbolo;
                        }
                    }
                    break;
                case "procedimento":
                    if(simbolo instanceof ProcedimentoImpl){
                        if(simbolo.getToken() == token){
                            aux = simbolo;
                        }
                    }
                    break;
                case "função":
                    if(simbolo instanceof FuncaoImpl){
                        if(simbolo.getToken() == token){
                            aux = simbolo;
                        }
                    }
                    break;
                case "Constante":
                    if(simbolo instanceof ConstanteImpl){
                        if(simbolo.getToken() == token){
                            aux = simbolo;
                        }
                    }
                    break;
            }
        }

        return aux;
    }

    public ArrayList<IIdentificador> getSimbolos(Token token,String tipo){

        /*if(this.tabelaSimbolos.isEmpty()){
            return null;
        }*/
        ArrayList<IIdentificador> aux = new ArrayList<>();
        for(IIdentificador simbolo: this.tabelaSimbolos.values()){
            switch (tipo){
                case "variavel":
                    if(simbolo instanceof VariaveisImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            System.out.println("aa");
                            aux.add(simbolo);
                        }
                    }
                    break;
                case "procedimento":
                    if(simbolo instanceof ProcedimentoImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux.add(simbolo);
                        }
                    }
                    break;
                case "função":
                    if(simbolo instanceof FuncaoImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux.add(simbolo);
                        }
                    }
                    break;
                case "Constante":
                    if(simbolo instanceof ConstanteImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux.add(simbolo);
                        }
                    }
                    break;
            }
        }

        return aux;
    }

    public IIdentificador getSimboloL(Token token,String tipo){
        if(this.tabelaSimbolos.isEmpty()){
            return null;
        }
        IIdentificador aux = null;
        for(IIdentificador simbolo: this.tabelaSimbolos.values()){
            switch (tipo){
                case "variavel":
                    if(simbolo instanceof VariaveisImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux = simbolo;
                        }
                    }
                    break;
                case "procedimento":
                    if(simbolo instanceof ProcedimentoImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux = simbolo;
                        }
                    }
                    break;
                case "função":
                    if(simbolo instanceof FuncaoImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux = simbolo;
                        }
                    }
                    break;
                case "constante":
                    if(simbolo instanceof ConstanteImpl){
                        if(simbolo.getToken().getLexema().equals(token.getLexema())){
                            aux = simbolo;
                        }
                    }
                    break;
            }
        }

        return aux;
    }

    public int numeroSimbolos(){
        return this.tabelaSimbolos.size();
    }


}

package modules.AnalisadorSintatico;

import domain.entities.Token;

import java.util.ArrayList;

public class ControllerAnalisadorSintatico {

    public Token token;
    public ArrayList<Token> listaTokens;
    public int indiceTokenAtual = 0;

    public void iniciarLeitura(ArrayList<Token> tokens){
        this.listaTokens = tokens;
        this.token = tokens.get(this.indiceTokenAtual);
    }

    public void proximo_token(){
        this.indiceTokenAtual++;

        if(this.indiceTokenAtual < listaTokens.size()){
            this.token = listaTokens.get(this.indiceTokenAtual);
        }else{
            this.token = null;
        }
    }

    public void init(){

        if(this.token.getLexema() == "procedure"){
            this.proximo_token();

            if(this.token.getLexema() == "start"){
                this.proximo_token();

                if(this.token.getLexema() == "{"){
                    this.start();
                    if(this.token.getLexema() == "}"){

                    }else{
                        System.out.println("Erro");
                    }
                }else{
                    System.out.println("Erro");
                }
            }else{
                System.out.println("Erro");
            }
        }else {
            System.out.println("Erro");
        }
    }

    public void start(){
        if(token != null) {
            this.program();
            this.start();
        }
    }

    public void program(){
        this.statement();
    }

    public void  statement(){

    }
}

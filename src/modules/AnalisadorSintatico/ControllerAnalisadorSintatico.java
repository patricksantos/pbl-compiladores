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
        if(token.getLexema() == "{"){
            proximo_token();
            statementsList();
            if(token.getLexema() == "}"){
                proximo_token();
            }
            else{
                System.out.println("ERRO");
            }
        }else if(primeiroSimpleStatement().contains(token.getLexema()) || token.getTipo() == "IDE"){
            simpleStatement();
        }
    }

    public void statementsList(){
        if(token.getLexema() == "{" || primeiroSimpleStatement().contains(token.getLexema()) || token.getTipo() == "IDE"){
            statement();
            statementsList();
        }

    }

    public void statementList(){
        //Fazer uma forma de aceitar se só tiver { }.
        if(token.getLexema() == "if"){

        }else if(token.getLexema() == "while"){

        }else{
            simpleStatement();
        }

    }

    public void simpleStatement(){
        if(token.getLexema() == "read"){
            proximo_token();
            procedimentoRead();
        }else if(token.getLexema() == "print"){
            proximo_token();
            procedimentoPrint();
        }else if(token.getLexema() == "if"){
            procedimentoIf();
        }else if(token.getLexema() == "while"){
            proximo_token();
            procedimentoWhile();
        }else if(token.getLexema() == "function"){
            proximo_token();
            procedimentoDeclFunction();
        }else if( token.getLexema() == "procedure"){
            proximo_token();
            procedimentoDeclProcedure();
        }else if(token.getLexema() == "typedef"){

        }else if(token.getLexema() == "var" || token.getLexema() == "const"){
            //proximo_token();
            procedimentoVarDecl();
        }else if(token.getLexema() == "const"){

        }else if(token.getTipo() == "IDE"){

        }
    }

    public void procedimentoIf(){
        if(token.getLexema() == "("){
            proximo_token();
            procedimentoExpression();
            if(token.getLexema() == ")"){
                proximo_token();
                if(token.getLexema() == "then"){
                    proximo_token();
                    statement();
                    if(token.getLexema() == "else"){
                        statement();
                    }
                }else{
                    System.out.println("ERRO");
                }
            }
            else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoWhile(){

        if(token.getLexema() == "("){
            proximo_token();
            procedimentoExpression();
            if(token.getLexema() == ")"){
                proximo_token();
                statement();
            }
            else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoExpression(){

    }

    public void procedimentoRead(){
        if(token.getLexema() == "("){
            proximo_token();
            expressionRead();
            if(token.getLexema() == ")"){
                proximo_token();
                if(token.getLexema() == ";"){
                    proximo_token();
                }else{
                    System.out.println("ERRO");
                }
            }
            else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void expressionRead(){
        if(token.getTipo() == "IDE"){
            proximo_token();
            if(token.getLexema() == "["){
                proximo_token();
                //Array ou matriz
                moreReadings();
            }else if(token.getLexema() == "."){
                proximo_token();
                //struct
                moreReadings();
            }else if(token.getLexema() == ","){
                moreReadings();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void moreReadings(){
        if(token.getLexema() == ","){
            proximo_token();
            expressionRead();
        }else if(token == null){
            //referente ao <> da gramatica
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoPrint(){
        if(token.getLexema() == "("){
            proximo_token();
            expressionPrint();
            if(token.getLexema() == ")"){
                proximo_token();
                if(token.getLexema() == ";"){
                    proximo_token();
                }else{
                    System.out.println("ERRO");
                }
            }
            else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void expressionPrint(){
        if(token.getTipo() == "IDE"){
            proximo_token();
            if(token.getLexema() == "["){
                proximo_token();
                //Array ou matriz
                moreExpressions();
            }else if(token.getLexema() == "."){
                proximo_token();
                //struct
                moreExpressions();
            }else if(token.getLexema() == ","){
                moreExpressions();
            }else{
                System.out.println("ERRO");
            }
        }else if(token.getTipo() == "CAD"){
            proximo_token();
            moreExpressions();
        }else{
            System.out.println("ERRO");
        }
    }

    public void moreExpressions(){
        if(token.getLexema() == ","){
            proximo_token();
            expressionPrint();
        }else if(token == null){
            //referente ao <> da gramatica
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoDeclProcedure(){
        if(token.getTipo() == "IDE"){
            proximo_token();
            if(token.getLexema() == "("){
                proximo_token();
                procedimentoParams();
                if(token.getLexema() == ")"){
                    proximo_token();
                    procedimentoBlockProc();
                }else{
                    System.out.println("ERRO");
                }
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoBlockProc(){
        if(token.getLexema() == "{"){
            proximo_token();
            statementsList();
            if(token.getLexema() == "}"){
                proximo_token();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoDeclFunction(){
        if(primeiroType(token)){
            proximo_token();
            if(token.getTipo() == "IDE"){
                proximo_token();
                if(token.getLexema() == "("){
                    proximo_token();
                    procedimentoParams();
                    if(token.getLexema() == ")"){
                        proximo_token();
                        procedimentoBlockFunc();
                    }else{
                        System.out.println("ERRO");
                    }
                }else{
                    System.out.println("ERRO");
                }
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoBlockFunc(){
        if(token.getLexema() == "{"){
            proximo_token();
            statementsList();
            procedimentoReturn();
            if(token.getLexema() == "}"){
                proximo_token();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoReturn(){
        
        if(token.getLexema() == "return"){
            proximo_token();
            if(token.getTipo() == "IDE"){
                proximo_token();
            }else if(token.getTipo() == "CAD"){
                proximo_token();
            }else if(token.getTipo() == "NRO"){
                proximo_token();
            }else if(true/*chamada de função*/){
               proximo_token();
            }else if(token.getLexema() != ";"){
                System.out.println("ERRO");
            }
            if(token.getLexema() == ";"){
                proximo_token();
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoParams(){
        if(primeiroType(token)){
            proximo_token();
            procedimentoParam();
            if(token.getLexema() == ","){
                procedimentoParams();
            }
        }
    }

    public void procedimentoParam(){
        if(token.getTipo() == "IDE"){
            proximo_token();
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoVarDecl(){
        if(token.getLexema() == "var"){
            proximo_token();
            if(token.getLexema() == "{"){
                proximo_token();
                procedimentoTypedVariable();
            }else{
                System.out.println("ERRO");
            }
        }else if(token.getLexema() == "const"){
            proximo_token();
            if(token.getLexema() == "{"){
                proximo_token();
                procedimentoTypedConst();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }

    }

    public void procedimentoTypedVariable(){
        if(primeiroType(token)){
            proximo_token();
            procedimentoVariables();
            if(token.getLexema() == ";"){
                proximo_token();
                if(primeiroType(token)){
                    procedimentoTypedVariable();
                }
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoVariables(){
        if(token.getTipo() == "IDE"){
            if(token.getLexema() == "["){
                proximo_token();
                procedimentoArrayUsage();
                if(token.getLexema() == "="){
                    proximo_token();
                    if(token.getLexema() == "{"){
                        proximo_token();
                        procedimentoVarArgs();
                        if(token.getLexema() == "}"){
                            proximo_token();
                        }else{
                            System.out.println("ERRO");
                        }
                    }else{
                        System.out.println("ERRO");
                    }
                }
            }else if(token.getLexema() == "="){

            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoArrayUsage(){

        if(token.getTipo() == "NRO" || token.getTipo() == "IDE" || token.getLexema() == "true" || token.getLexema() == "false"){
            proximo_token();
            if(token.getLexema() == "]"){
                proximo_token();
                if(token.getLexema() == "["){
                    proximo_token();
                    if(token.getTipo() == "NRO" || token.getTipo() == "IDE" || token.getLexema() == "true" || token.getLexema() == "false"){
                        proximo_token();
                        if(token.getLexema() == "]"){
                            proximo_token();
                        }
                    }
                }
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoVarArgs(){

    }
    public void procedimentoVariableDeclarator(){

    }

    public void procedimentoTypedConst(){

    }

    public ArrayList<String> primeiroSimpleStatement(){
        ArrayList<String> listaPrimeiro = new ArrayList<>();
        listaPrimeiro.add("read");
        listaPrimeiro.add("print");
        listaPrimeiro.add("if");
        listaPrimeiro.add("while");
        listaPrimeiro.add("function");
        listaPrimeiro.add("typedef");
        listaPrimeiro.add("var");
        listaPrimeiro.add("const");
        return listaPrimeiro;
    }

    public boolean primeiroType(Token token){
        if(token.getLexema() == "int" || token.getLexema() == "real" || token.getLexema() == "boolean" || token.getLexema() == "string"){
            return true;
        }else{
            return false;
        }
    }
}

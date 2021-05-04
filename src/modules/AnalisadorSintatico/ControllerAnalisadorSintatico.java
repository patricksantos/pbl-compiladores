package modules.AnalisadorSintatico;

import domain.entities.Token;
import modules.AnalisadorSintatico.usecases.ErroSintatico;

import java.util.ArrayList;

public class ControllerAnalisadorSintatico {

    public Token token;
    public ArrayList<Token> listaTokens;
    public int indiceTokenAtual = 0;
    public Token tokenFimArquivo;
    public ArrayList<ErroSintatico> erros;

    public void iniciarLeitura(ArrayList<Token> tokens){
        this.listaTokens = tokens;
        this.token = tokens.get(this.indiceTokenAtual);
        this.tokenFimArquivo = new Token("$","EOF",false);
        this.listaTokens.add(tokenFimArquivo);
        this.erros = new ArrayList<>();
        this.init();
    }

    public void proximo_token(){
        System.out.println(indiceTokenAtual);
        this.indiceTokenAtual++;

        if(this.indiceTokenAtual < listaTokens.size()){
            this.token = listaTokens.get(this.indiceTokenAtual);
        }else{
            this.listaTokens.add(tokenFimArquivo);
            this.token = listaTokens.get(this.indiceTokenAtual);
        }
    }

    public void init(){

        if(this.token.getLexema().equals("procedure")){
            this.proximo_token();

            if(this.token.getLexema().equals("start")){
                this.proximo_token();

                if(this.token.getLexema().equals("{")){
                    proximo_token();
                    if(this.token.getLexema().equals("}")){
                        proximo_token();
                    }else{
                        this.start();
                        if(this.token.getLexema().equals("}")){
                            proximo_token();
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                            System.out.println(erro.info());
                        }
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    System.out.println(erro.info());
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"start",token.getLexema());
                System.out.println(erro.info());
            }
        }else {
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"procedure",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void start(){
        //Fim do arquivo
        if(token.getLexema().equals("}")) {

        }else{
            this.program();
            this.start();
        }
    }

    public void program(){

        this.statement();
    }

    public void  statement(){

        if(this.token.getLexema().equals("{")){
            proximo_token();
            statementsList();
            if(this.token.getLexema().equals("}")){
                proximo_token();
            }
            else{
                System.out.println("ERRO statement");
            }
        }else if(primeiroSimpleStatement().contains(token.getLexema()) || this.token.getTipo().equals("IDE")){
            simpleStatement();
        }
    }

    public void statementsList(){
        if(this.token.getLexema().equals("{") || primeiroSimpleStatement().contains(token.getLexema()) || this.token.getTipo().equals("IDE")){
            statement();
            statementsList();
        }

    }

    public void statementList(){
        //Fazer uma forma de aceitar se só tiver { }.
        if(this.token.getLexema().equals("if")){

        }else if(this.token.getLexema().equals("while")){

        }else{
            simpleStatement();
        }

    }

    public void simpleStatement(){
        if(this.token.getLexema().equals("read")){
            proximo_token();
            procedimentoRead();
        }else if(this.token.getLexema().equals("print")){
            proximo_token();
            procedimentoPrint();
        }else if(this.token.getLexema().equals("if")){
            proximo_token();
            procedimentoIf();
        }else if(this.token.getLexema().equals("while")){
            proximo_token();
            procedimentoWhile();
        }else if(token.getLexema().equals("function")){
            proximo_token();
            procedimentoDeclFunction();
        }else if( token.getLexema().equals("procedure")){
            proximo_token();
            procedimentoDeclProcedure();
        }else if(token.getLexema().equals("typedef")){

        }else if(token.getLexema().equals("var") || token.getLexema().equals("const")){
            //proximo_token();
            procedimentoVarDecl();
        }else if(token.getLexema().equals("const")){

        }else if(token.getTipo().equals("IDE")){

        }else{
            System.out.println("ERRO simplestatement");
        }
    }

    public void procedimentoIf(){
        if(token.getLexema().equals("(")){
            proximo_token();
            procedimentoExpression();
            if(token.getLexema().equals(")")){
                proximo_token();
                if(token.getLexema().equals("then")){
                    proximo_token();
                    statement();
                    if(token.getLexema().equals("else")){
                        statement();
                    }
                }else{
                    System.out.println("ERRO if sem then ");
                }
            }
            else{
                System.out.println("ERRO if sem )");
            }
        }else{
            System.out.println("ERRO if sem (");
        }
    }

    public void procedimentoWhile(){

        if(token.getLexema().equals("(")){
            proximo_token();
            procedimentoExpression();
            if(token.getLexema().equals(")")){
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
        if(token.getLexema().equals("true") || token.getLexema().equals("false")){
            proximo_token();
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoRead(){
        if(token.getLexema().equals("(")){
            proximo_token();
            expressionRead();
            if(token.getLexema().equals(")")){
                proximo_token();
                if(token.getLexema().equals(";")){
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
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals("[")){
                proximo_token();
                //Array ou matriz
                moreReadings();
            }else if(token.getLexema().equals(".")){
                proximo_token();
                //struct
                moreReadings();
            }else if(token.getLexema().equals(",")){
                moreReadings();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void moreReadings(){
        if(token.getLexema().equals(",")){
            proximo_token();
            expressionRead();
        }else if(token == null){
            //referente ao <> da gramatica
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoPrint(){
        if(token.getLexema().equals("(")){
            proximo_token();
            expressionPrint();
            if(token.getLexema().equals(")")){
                proximo_token();
                if(token.getLexema().equals(";")){
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
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals("[")){
                proximo_token();
                //Array ou matriz
                moreExpressions();
            }else if(token.getLexema().equals(".")){
                proximo_token();
                //struct
                moreExpressions();
            }else if(token.getLexema().equals(",")){
                moreExpressions();
            }else{
                System.out.println("ERRO");
            }
        }else if(token.getTipo().equals("CAD")){
            proximo_token();
            moreExpressions();
        }else{
            System.out.println("ERRO");
        }
    }

    public void moreExpressions(){
        if(token.getLexema().equals(",")){
            proximo_token();
            expressionPrint();
        }else if(token == null){
            //referente ao <> da gramatica
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoDeclProcedure(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals("(")){
                proximo_token();
                procedimentoParams();
                if(token.getLexema().equals(")")){
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
        if(token.getLexema().equals("{")){
            proximo_token();
            statementsList();
            if(token.getLexema().equals("}")){
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
            if(token.getTipo().equals("IDE")){
                proximo_token();
                if(token.getLexema().equals("(")){
                    proximo_token();
                    procedimentoParams();
                    if(token.getLexema().equals(")")){
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
        if(token.getLexema().equals("{")){
            proximo_token();
            statementsList();
            procedimentoReturn();
            if(token.getLexema().equals("}")){
                proximo_token();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoReturn(){
        
        if(token.getLexema().equals("return")){
            proximo_token();
            if(token.getTipo().equals("IDE")){
                proximo_token();
            }else if(token.getTipo().equals("CAD")){
                proximo_token();
            }else if(token.getTipo().equals("NRO")){
                proximo_token();
            }else if(true/*chamada de função*/){
               proximo_token();
            }else if(!token.getLexema().equals(";")){
                System.out.println("ERRO");
            }
            if(token.getLexema().equals(";")){
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
            if(token.getLexema().equals(",")){
                procedimentoParams();
            }
        }
    }

    public void procedimentoParam(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoCallFunc(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals("(")){
                proximo_token();
                procedimentoArgs();
                if(token.getLexema().equals(")")){
                    proximo_token();
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

    public void procedimentoArgs(){
        if(token.getTipo().equals("NRO") || token.getTipo().equals("NRO") || token.getLexema().equals("true") || token.getLexema().equals("false")){
            procedimentoArg();
            if(token.getLexema().equals(",")){
                procedimentoArgs();
            }
        }
    }

    public void procedimentoArg(){
        if(token.getTipo().equals("IDE")){
            Token tokenAux = listaTokens.get(indiceTokenAtual+1);
            if(tokenAux.getLexema().equals("(")){
                procedimentoCallFunc();
            }else{
                proximo_token();
            }
        }else if(token.getTipo().equals("NRO") || token.getLexema().equals("true") || token.getLexema().equals("false")){
            proximo_token();
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoVarDecl(){
        if(token.getLexema().equals("var")){
            proximo_token();
            if(token.getLexema().equals("{")){
                proximo_token();
                procedimentoTypedVariable();
            }else{
                System.out.println("ERRO");
            }
        }else if(token.getLexema().equals("const")){
            proximo_token();
            if(token.getLexema().equals("{")){
                proximo_token();
                procedimentoTypedConst();
            }else{
                System.out.println("ERRO");
            }
        }else{
            System.out.println("ERRO");
        }

    }

    public void procedimentoVariableInit(){

    }

    public void procedimentoTypedVariable(){
        if(primeiroType(token)){
            proximo_token();
            procedimentoVariables();
            if(token.getLexema().equals(";")){
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
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals("[")){
                proximo_token();
                procedimentoArrayUsage();
                if(token.getLexema().equals("=")){
                    proximo_token();
                    if(token.getLexema().equals("{")){
                        proximo_token();
                        procedimentoVarArgs();
                        if(token.getLexema().equals("}")){
                            proximo_token();
                        }else{
                            System.out.println("ERRO");
                        }
                    }else{
                        System.out.println("ERRO");
                    }
                }
            }else if(token.getLexema().equals("=")){

            }
        }else{
            System.out.println("ERRO");
        }
    }

    public void procedimentoArrayUsage(){

        if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
            proximo_token();
            if(token.getLexema().equals("]")){
                proximo_token();
                if(token.getLexema().equals("[")){
                    proximo_token();
                    if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
                        proximo_token();
                        if(token.getLexema().equals("]")){
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
        if(token.getLexema().equals("true") || token.getLexema().equals("false")
                || token.getTipo().equals("NRO") || token.getTipo().equals("CAD")){
            proximo_token();
            if(token.getLexema().equals(",")){
                procedimentoVarArgs();
            }

        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,true,false,NRO,CAD",token.getLexema());
            System.out.println(erro.info());
        }
    }

    /*public void procedimentoVarArg(){

    }
    public void procedimentoVariableDeclarator(){

    }*/

    public void procedimentoTypedConst(){
        if(primeiroType(token)){
            proximo_token();
            procedimentoConstants();
            if(token.getLexema().equals(";")){
                proximo_token();
                if(primeiroType(token)){
                    procedimentoTypedConst();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"int,real,boolean,string",token.getLexema());
            System.out.println(erro.info());
        }

    }

    public void procedimentoConstants(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals("=")){
                proximo_token();
                if(token.getLexema().equals("true") || token.getLexema().equals("false")
                        || token.getTipo().equals("NRO") || token.getTipo().equals("CAD")){
                    proximo_token();
                    if(token.getLexema().equals(",")){
                        proximo_token();
                        procedimentoConstants();
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,true,false,NRO,CAD",token.getLexema());
                    System.out.println(erro.info());
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"=",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoStructUsage(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals(".")){
                proximo_token();
                if(token.getTipo().equals("IDE")){
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoVariableUsage(){
        if(token.getTipo().equals("IDE")){
            Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
            if(tokenAux.getLexema().equals(".")){
                procedimentoStructUsage();
                if(token.getLexema().equals(";")){
                    proximo_token();
                }else if(token.getLexema().equals("=")){
                    proximo_token();
                    if(token.getTipo().equals("IDE")){
                        tokenAux = listaTokens.get(indiceTokenAtual + 1);
                        if(tokenAux.getLexema().equals(".")){
                            procedimentoStructUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                System.out.println("ERRO");
                            }
                        }else if(tokenAux.getLexema().equals("[")){
                            proximo_token();
                            proximo_token();
                            procedimentoArrayUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                System.out.println("ERRO");
                            }
                        }else if(tokenAux.getLexema().equals("(")){
                            procedimentoCallFunc();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                System.out.println("ERRO");
                            }
                        }else if(true){
                            procedimentoVariableInit();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                System.out.println("ERRO");
                            }
                        }else{
                            System.out.println("ERRO");
                        }
                    }
                }

            }else if(tokenAux.getLexema().equals("[")){
                proximo_token();
                proximo_token();
                procedimentoArrayUsage();
                if(token.getLexema().equals(";")){
                    proximo_token();
                }else if(token.getLexema().equals("=")){
                    proximo_token();
                    if(token.getTipo().equals("IDE")){
                        tokenAux = listaTokens.get(indiceTokenAtual + 1);
                        if(tokenAux.getLexema().equals(".")){
                            procedimentoStructUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("[")){
                            proximo_token();
                            proximo_token();
                            procedimentoArrayUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }
                        }else{
                            System.out.println("ERRO");
                        }
                    }else if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
                        proximo_token();
                    }else if(token.getLexema().equals("{")){
                        proximo_token();
                        procedimentoVarArgs();
                        if(token.getLexema().equals("}")){
                            proximo_token();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }
                        }
                    }
                }
            }else if(tokenAux.getLexema().equals("=")){

            }
        }

    }

    public void procedimentoStructDecl(){
        if(token.getLexema().equals("typedef")){
            proximo_token();
            if(token.getLexema().equals("struct")){
                proximo_token();
                if(token.getLexema().equals("extends")){
                    proximo_token();
                    if(token.getTipo().equals("IDE")){
                        proximo_token();
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                        System.out.println(erro.info());
                    }
                }
                if(token.getLexema().equals("{")){
                    proximo_token();
                    procedimentoStructDef();
                    if(token.getLexema().equals("}")){
                        proximo_token();
                        if(token.getTipo().equals("IDE")){
                            proximo_token();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                            System.out.println(erro.info());
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        System.out.println(erro.info());
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    System.out.println(erro.info());
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"struct",token.getLexema());
                System.out.println(erro.info());
            }

        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"typedef",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoStructDef(){
        if(token.getLexema().equals("var") || token.getLexema().equals("const")){
            procedimentoVarDecl();
            if(token.getLexema().equals("var") || token.getLexema().equals("const")){
                procedimentoStructDef();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"var,const",token.getLexema());
            System.out.println(erro.info());
        }
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

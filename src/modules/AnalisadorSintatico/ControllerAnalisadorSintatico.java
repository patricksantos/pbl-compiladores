package modules.AnalisadorSintatico;

import domain.entities.Token;
import modules.AnalisadorSintatico.usecases.ErroSintatico;

import java.util.ArrayList;

public class ControllerAnalisadorSintatico {

    public Token token;
    public ArrayList<Token> listaTokens;
    public ArrayList<Token> listaTokensAuxilixar;
    public int indiceTokenAtual = 0;
    public Token tokenFimArquivo;
    public ArrayList<ErroSintatico> erros;

    public void iniciarLeitura(ArrayList<Token> tokens){
        this.indiceTokenAtual = 0;
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
            //listaTokensAuxilixar.add(token);
        }else{
            this.listaTokens.add(tokenFimArquivo);
            this.token = listaTokens.get(this.indiceTokenAtual);
            //listaTokensAuxilixar.add(this.token);
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
                            ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                            Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                            token.setError(error);
                            listaTokensAuxilixar.add(token);
                            System.out.println(error.info());
                        }
                    }
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                    token.setError(error);
                    listaTokensAuxilixar.add(token);
                    System.out.println(error.info());
                }
            }else{
                ErroSintatico error = new ErroSintatico(token.getLinha(),"start",token.getLexema());
                Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                token.setError(error);
                listaTokensAuxilixar.add(token);
                System.out.println(error.info());
            }
        }else {
            ErroSintatico error = new ErroSintatico(token.getLinha(),"procedure",token.getLexema());
            Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
            token.setError(error);
            listaTokensAuxilixar.add(token);
            System.out.println(error.info());
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

    public void statement(){

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
            procedimentoStructDecl();
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
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"then",token.getLexema());
                    System.out.println(erro.info());
                }
            }
            else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            System.out.println(erro.info());
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
                ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            System.out.println(erro.info());
        }
    }

//    <Literal> ::= true
//            | false
//            | DecimalLiteral
//            | RealLiteral
//            | StringLiteral
//
//            <Primary> ::= Identifier
//           | <Literal>
//
//    DecimalLiteral = {Digit}+
//    RealLiteral = {Digit}+'.'{Digit}+
//    StringLiteral = '"' ( {String Chars} | '\' {Printable} )* '"'

    public void procedimentoExpression(){
        OrExpression();
    }

    public void OrExpression(){
        AndExpression();
        if(token.getLexema().equals("||") && token.getTipo().equals("LOG")){
            proximo_token();
            OrExpression();
        }
    }

    public void AndExpression(){
        EqualityExpression();
        if(token.getLexema().equals("&&") && token.getTipo().equals("LOG")){
            proximo_token();
            AndExpression();
        }
    }

    public void EqualityExpression(){
        CompareExpression();
        if(token.getLexema().equals("==") || token.getLexema().equals("!=") && token.getTipo().equals("REL")){
            proximo_token();
            EqualityExpression();
        }
    }

    public void CompareExpression(){
        AddExpression();
        if(token.getLexema().equals("<") || token.getLexema().equals(">") || token.getLexema().equals("<=") || token.getLexema().equals(">=")){
            proximo_token();
            CompareExpression();
        }
    }

    public void AddExpression(){
        MultiplicationExpression();
        if(token.getLexema().equals("+") || token.getLexema().equals("-")){
            proximo_token();
            AddExpression();
        }
    }

    public void MultiplicationExpression(){
        UnaryExpression();
        if(token.getLexema().equals("*") || token.getLexema().equals("/")){
            proximo_token();
            MultiplicationExpression();
        }
    }

    public void UnaryExpression(){
        if(token.getLexema().equals("!")){
            proximo_token();
            UnaryExpression();
        }else{
            ObjectExpression();
        }
    }

    public void ObjectExpression(){
        if(token.getLexema().equals("true") || token.getLexema().equals("false")){
            MethodExpression();
            //! <PrimaryArrayCreationExpression> ::= <>!Empty
        }else{
            System.out.println("ERRO");
        }
    }
    public void MethodExpression(){
        PrimaryExpression();
    }

    public void PrimaryExpression(){
        if(token.getLexema().equals("true") || token.getLexema().equals("false") ||
                token.getTipo().equals("NRO") || token.getTipo().equals("CAD"))
        {
            proximo_token();
        }else if(token.getLexema().equals("(")){
            proximo_token();
            procedimentoExpression();
            if(token.getLexema().equals(")")){
                proximo_token();
            }
        }
        else{
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
        }/*else if(token == null){
            //referente ao <> da gramatica
        }*/else{
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
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                    System.out.println(erro.info());
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoBlockProc(){
        if(token.getLexema().equals("{")){
            proximo_token();
            statementsList();
            if(token.getLexema().equals("}")){
                proximo_token();
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
            System.out.println(erro.info());
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
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                        System.out.println("-" + erro.info());
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
                    System.out.println(erro.info());
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"boolean,int,real,string",token.getLexema());
            System.out.println(erro.info());
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
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                System.out.println(erro.info());
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoReturn(){
        
        if(token.getLexema().equals("return")){
            proximo_token();
            if(token.getTipo().equals("IDE")){
                Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
                if(tokenAux.getLexema().equals("(")){
                    procedimentoCallFunc();
                }else{
                    proximo_token();
                }
            }else if(token.getTipo().equals("CAD")){
                proximo_token();
            }else if(token.getTipo().equals("NRO")){
                proximo_token();
            }else if(!token.getLexema().equals(";")){
                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                System.out.println(erro.info());
            }
            if(token.getLexema().equals(";")){
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"return",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoParams(){
        if(primeiroType(token)){
            proximo_token();
            procedimentoParam();
            if(token.getLexema().equals(",")){
                procedimentoParams();
            }
        }else if(token.getLexema().equals(")")){

        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"boolean,int,real,string",token.getLexema());
            System.out.println(erro.info());
        }
    }

    public void procedimentoParam(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
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
                if(primeiroType(token)){
                    procedimentoTypedVariable();
                    if(token.getLexema().equals("}")){
                        proximo_token();
                    }else{
                        ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        System.out.println(error.info());
                    }
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
                    System.out.println(error.info());
                }
            }else{
                ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                System.out.println(error.info());
            }
        }else if(token.getLexema().equals("const")){
            proximo_token();
            if(token.getLexema().equals("{")){
                proximo_token();
                if(primeiroType(token)){
                    procedimentoTypedConst();
                    if(token.getLexema().equals("}")){
                        proximo_token();
                    }else{
                        ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        System.out.println(error.info());
                    }
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
                    System.out.println(error.info());
                }
            }else{
                ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                System.out.println(error.info());
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"var,const",token.getLexema());
            System.out.println(error.info());
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
                ErroSintatico error = new ErroSintatico(token.getLinha(),";",token.getLexema());
                System.out.println(error.info());
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
            System.out.println(error.info());
        }
    }

    public void procedimentoVariables(){
        if(token.getTipo().equals("IDE")){
            procedimentoVariableDeclarator();
            if(token.getLexema().equals(",")){
                proximo_token();
                procedimentoVariables();
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(error.info());
        }
    }

    public void procedimentoVariableDeclarator(){
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
                            ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                            System.out.println(error.info());
                        }
                    }else{
                        ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                        System.out.println(error.info());
                    }
                }
            }else if(token.getLexema().equals("=")){
                proximo_token();
                if(token.getTipo().equals("IDE")){
                    Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
                    if(tokenAux.getLexema().equals(".")){
                        procedimentoStructUsage();
                    }else if(tokenAux.getLexema().equals("(")){
                        procedimentoCallFunc();
                    }else if(tokenAux.getLexema().equals("local") || tokenAux.getLexema().equals("global")){
                        proximo_token();
                        proximo_token();
                        if(token.getLexema().equals(".")){
                            proximo_token();
                            if(token.getTipo().equals("IDE")){
                                proximo_token();
                            }else{
                                ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                System.out.println(error.info());
                            }
                        }else{
                            ErroSintatico error = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            System.out.println(error.info());
                        }
                    }
                }else if(false){
                    //Terminar
                    procedimentoVariableInit();
                }
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(error.info());
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
    */

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
                        if(token.getLexema().equals(";")){
                            proximo_token();
                        }else{
                            System.out.println("ERRO");
                        }
                    }else if(token.getLexema().equals("{")){
                        proximo_token();
                        procedimentoVarArgs();
                        if(token.getLexema().equals("}")){
                            proximo_token();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }
                        }
                    }else{
                        System.out.println("ERRO");
                    }
                }else{
                    System.out.println("ERRO");
                }
            }else if(tokenAux.getLexema().equals("=")){
                    proximo_token();
                    proximo_token();
                    if(true){
                        //VariableInit
                        //Colocar ; no final
                    }else if(token.getTipo().equals("IDE")){
                        tokenAux = listaTokens.get(indiceTokenAtual + 1);
                        if(tokenAux.getLexema().equals(".")){
                            procedimentoStructUsage();
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
                        }else{
                            System.out.println("ERRO");
                        }

                    }else if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                        proximo_token();
                        if(token.getLexema().equals(".")){
                            proximo_token();
                            if(token.getTipo().equals("IDE")){
                                proximo_token();
                                if(token.getLexema().equals(";")){
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
                    }else{
                        System.out.println("ERRO");
                    }
            }
        }

    }

    public void procedimentoVariableScope(){
        Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
        if(token.getLexema().equals("local") || token.getLexema().equals("global")){
            //Começo VariableScope
            proximo_token();
            if(token.getLexema().equals(".")){
                proximo_token();
                if(token.getTipo().equals("IDE")){
                    tokenAux = listaTokens.get(indiceTokenAtual + 1);
                    if(tokenAux.getLexema().equals("=")){
                        proximo_token();
                        proximo_token();
                        if(token.getTipo().equals("IDE")){
                            tokenAux = listaTokens.get(indiceTokenAtual + 1);
                            if(tokenAux.getLexema().equals(";")){
                                proximo_token();
                                proximo_token();
                            }else if(tokenAux.getLexema().equals(".")){
                                procedimentoStructUsage();
                            }else if(tokenAux.getLexema().equals("[")){
                                proximo_token();
                                proximo_token();
                                procedimentoArrayUsage();
                            }else if(tokenAux.getLexema().equals("local") || tokenAux.getLexema().equals("global")){
                                proximo_token();
                                proximo_token();
                                if(token.getLexema().equals(".")){
                                    proximo_token();
                                    if(token.getTipo().equals("IDE")){
                                        proximo_token();
                                        if(token.getLexema().equals("[")){
                                            procedimentoArrayUsage();
                                            if(token.getLexema().equals(";")){
                                                proximo_token();
                                            }else{
                                                System.out.println("ERRO");
                                            }
                                        }else if(token.getLexema().equals(";")){
                                            proximo_token();
                                        }else{
                                            System.out.println("ERRO");
                                        }
                                    }
                                }
                            }
                        }

                    }else if(tokenAux.getLexema().equals("[")){
                        proximo_token();
                        proximo_token();
                        procedimentoArrayUsage();
                        if(token.getLexema().equals("=")){
                            proximo_token();
                            if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                                if(token.getLexema().equals(".")){
                                    proximo_token();
                                    if(token.getTipo().equals("IDE")){
                                        proximo_token();
                                        if(token.getLexema().equals(";")){
                                            proximo_token();
                                        }else{
                                            System.out.println("ERRO");
                                        }
                                    }
                                }
                            }

                        }

                    }else if(tokenAux.getLexema().equals(".")){//chamada de variableUsage em VariableScoope
                        procedimentoVariableUsage();
                    }
                }

            }
        }else if(token.getTipo().equals("IDE")){

            tokenAux = listaTokens.get(indiceTokenAtual + 1);
            if(tokenAux.getLexema().equals("[")){
                proximo_token();
                proximo_token();
                procedimentoArrayUsage();
                if(token.getLexema().equals("=")){
                    proximo_token();
                    if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                        proximo_token();
                        if(token.getLexema().equals(".")) {
                            proximo_token();
                            if (token.getTipo().equals("IDE")) {
                                proximo_token();
                                if (token.getLexema().equals(";")) {
                                    proximo_token();
                                }
                            }
                        }
                    }
                }else{
                    System.out.println("ERRO");
                }

            }else if(tokenAux.getLexema().equals(".")){
                procedimentoStructUsage();
                if(token.getLexema().equals("=")){
                    proximo_token();
                    if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                        proximo_token();
                        if(token.getLexema().equals(".")) {
                            proximo_token();
                            if (token.getTipo().equals("IDE")) {
                                proximo_token();
                                if (token.getLexema().equals(";")) {
                                    proximo_token();
                                }
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
        listaPrimeiro.add("procedure");
        return listaPrimeiro;
    }

    public boolean primeiroType(Token token){
        if(token.getLexema().equals("int") || token.getLexema().equals("real") || token.getLexema().equals("boolean") || token.getLexema().equals("string")){
            return true;
        }else{
            return false;
        }
    }
}

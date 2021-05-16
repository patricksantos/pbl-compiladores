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
                            //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                            //token.setError(error);
                            //listaTokensAuxilixar.add(token);
                            System.out.println(error.info());
                            proximo_token();
                        }
                    }
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                    //token.setError(error);
                    //listaTokensAuxilixar.add(token);
                    System.out.println(error.info());
                    proximo_token();
                }
            }else{
                ErroSintatico error = new ErroSintatico(token.getLinha(),"start",token.getLexema());
                //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                //token.setError(error);
                //listaTokensAuxilixar.add(token);
                System.out.println(error.info());
                proximo_token();
            }
        }else {
            ErroSintatico error = new ErroSintatico(token.getLinha(),"procedure",token.getLexema());
            //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
            //token.setError(error);
            //listaTokensAuxilixar.add(token);
            System.out.println(error.info());
            proximo_token();
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
                proximo_token();
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
            Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
            if(tokenAux.getLexema().equals("(")){
                procedimentoCallFunc();
                if(token.getLexema().equals(";")){
                    proximo_token();
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }else if(tokenAux.getLexema().equals("local") || tokenAux.getLexema().equals("global")){
                procedimentoVariableScope();
            }else if(tokenAux.getLexema().equals(".") || tokenAux.getLexema().equals("[")
                    || tokenAux.getLexema().equals("=") || tokenAux.getLexema().equals(";")){
                procedimentoVariableUsage();
            }else{
                //Talvez não tenha erro
                proximo_token();
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,local,global,.,[,=,;",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"read,print,while,if,function,procedure,typedef,var,const,IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
    }
/*    <SimpleStatament> ::=  <Read Statement>-
                  | <Print Statement> -
                  | <VariableUsage>
                  | <VariableScope> -
                  | <Decl>-
                  | <Struct Decl>-
                  | <Var Decl>-
                  | <Call Func> ';'-
            |<if>-
            |<While>-
           */

    public void procedimentoIf(){
        if(token.getLexema().equals("(")){
            proximo_token();
            if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                    || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!") || token.getTipo().equals("IDE")) {
                procedimentoExpression();
                if(token.getLexema().equals(")")){
                    proximo_token();
                    if(token.getLexema().equals("then")){
                        proximo_token();
                        statement();
                        if(token.getLexema().equals("else")){
                            proximo_token();
                            statement();
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"then",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }
                else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD,(,!,IDE",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }

        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoWhile(){
        if(token.getLexema().equals("(")){
            proximo_token();
            if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                    || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!") || token.getTipo().equals("IDE")){
                procedimentoExpression();
                if(token.getLexema().equals(")")){
                    proximo_token();
                    if(token.getLexema().equals("{")) {
                        statement();
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }
                else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD,(,!,IDE",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
        MethodExpression();
        /*if(token.getLexema().equals("true") || token.getLexema().equals("false")){
            MethodExpression();
            //! <PrimaryArrayCreationExpression> ::= <>!Empty
        }else{
            proximo_token();
            System.out.println("ERRO");
        }*/
    }
    public void MethodExpression(){
        PrimaryExpression();
    }

    public void PrimaryExpression(){
        if(token.getLexema().equals("true") || token.getLexema().equals("false") ||
                token.getTipo().equals("NRO") || token.getTipo().equals("CAD") || token.getTipo().equals("IDE"))
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
            proximo_token();
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
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }
            else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
    }

    public void expressionRead(){
        if(token.getTipo().equals("IDE")){
            //proximo_token();
            Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
            if(tokenAux.getLexema().equals("[")){
                proximo_token();
                proximo_token();
                procedimentoArrayUsage();
                if(token.getLexema().equals(",")){
                    moreReadings();
                }
            }else if(tokenAux.getLexema().equals(".")){
                //proximo_token();
                procedimentoStructUsage();
                if(token.getLexema().equals(",")){
                    moreReadings();
                }
            }else if(tokenAux.getLexema().equals(",")){
                proximo_token();
                moreReadings();
            }else{
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
    }

    public void moreReadings(){
        if(token.getLexema().equals(",")){
            proximo_token();
            expressionRead();
        }/*else if(token == null){
            //referente ao <> da gramatica
        }*/else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),",",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }
            else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
    }

    public void expressionPrint(){
        if(token.getTipo().equals("IDE")){
            //proximo_token();
            Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
            if(tokenAux.getLexema().equals("[")){
                proximo_token();
                proximo_token();
                procedimentoArrayUsage();
                if(token.getLexema().equals(",")){
                    moreExpressions();
                }
            }else if(tokenAux.getLexema().equals(".")){
                //proximo_token();
                procedimentoStructUsage();
                if(token.getLexema().equals(",")){
                    moreExpressions();
                }
            }else if(tokenAux.getLexema().equals(",")){
                proximo_token();
                moreExpressions();
            }else{
                proximo_token();
            }
        }else if(token.getTipo().equals("CAD")){
            proximo_token();
            if(token.getLexema().equals(",")){
                moreExpressions();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,CAD",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
    }

    public void moreExpressions(){
        if(token.getLexema().equals(",")){
            proximo_token();
            expressionPrint();
        }/*else if(token == null){
            //referente ao <> da gramatica
        }*/else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),",",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"boolean,int,real,string",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
            proximo_token();
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
            proximo_token();
        }
    }

    public void procedimentoParam(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                    proximo_token();
                }
            }else{
                System.out.println("ERRO");
                proximo_token();
            }
        }else{
            System.out.println("ERRO");
            proximo_token();
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
            proximo_token();
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
                        proximo_token();
                    }
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
                    System.out.println(error.info());
                    proximo_token();
                }
            }else{
                ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                System.out.println(error.info());
                proximo_token();
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
                        proximo_token();
                    }
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
                    System.out.println(error.info());
                    proximo_token();
                }
            }else{
                ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                System.out.println(error.info());
                proximo_token();
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"var,const",token.getLexema());
            System.out.println(error.info());
            proximo_token();
        }

    }

    public void procedimentoVariableInit(){
        procedimentoExpression();
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
                proximo_token();
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
            System.out.println(error.info());
            proximo_token();
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
            proximo_token();
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
                        if(token.getLexema().equals("true") || token.getLexema().equals("false")
                                || token.getTipo().equals("NRO") || token.getTipo().equals("CAD")){
                            procedimentoVarArgs();
                            if(token.getLexema().equals("}")){
                                proximo_token();
                            }else{
                                ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                                System.out.println(error.info());
                                proximo_token();
                            }
                        }else{
                            ErroSintatico error = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD",token.getLexema());
                            System.out.println(error.info());
                            proximo_token();
                        }
                    }else{
                        ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                        System.out.println(error.info());
                        proximo_token();
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
                    }else if(primeiroOperadores(tokenAux)){
                        procedimentoVariableInit();
                    }else{
                        proximo_token();
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,.,&,|,/,*,-,+,>,<,=,!",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }else if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                    proximo_token();
                    if(token.getLexema().equals(".")){
                        proximo_token();
                        if(token.getTipo().equals("IDE")){
                            proximo_token();
                        }else{
                            ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                            System.out.println(error.info());
                            proximo_token();
                        }
                    }else{
                        ErroSintatico error = new ErroSintatico(token.getLinha(),".",token.getLexema());
                        System.out.println(error.info());
                        proximo_token();
                    }
                }else if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                        || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!")){
                    procedimentoVariableInit();
                }else{
                    ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE,local,global,true,false,NRO,CAD,(,!",token.getLexema());
                    System.out.println(error.info());
                    proximo_token();
                }
            }
        }else{
            ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(error.info());
            proximo_token();
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
                proximo_token();
            }
        }else{
            System.out.println("ERRO");
            proximo_token();
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
            proximo_token();
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
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"int,real,boolean,string",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"=",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("[")){
                            proximo_token();
                            proximo_token();
                            procedimentoArrayUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("(")){
                            procedimentoCallFunc();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(primeiroOperadores(tokenAux)){
                            procedimentoVariableInit();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            proximo_token();
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,.,&,|,/,*,-,+,>,<,=,!",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                            || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!")){
                        procedimentoVariableInit();
                        if(token.getLexema().equals(";")){
                            proximo_token();
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
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
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,true,false,NRO,CAD,(,!,local,global",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"=,;",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
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
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("[")){
                            proximo_token();
                            proximo_token();
                            procedimentoArrayUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            proximo_token();
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),".,[",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
                        proximo_token();
                        if(token.getLexema().equals(";")){
                            proximo_token();
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else if(token.getLexema().equals("{")){
                        proximo_token();
                        if(token.getLexema().equals("true") || token.getLexema().equals("false")
                                || token.getTipo().equals("NRO") || token.getTipo().equals("CAD")){
                            procedimentoVarArgs();
                            if(token.getLexema().equals("}")){
                                proximo_token();
                                if(token.getLexema().equals(";")){
                                    proximo_token();
                                }else{
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
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
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"local,global,{,IDE",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";,=",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }else if(tokenAux.getLexema().equals("=")){
                    proximo_token();
                    proximo_token();
                    if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                            || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!")){
                        procedimentoVariableInit();
                        if(token.getLexema().equals(";")){
                            proximo_token();
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
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
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(primeiroOperadores(tokenAux)){
                            procedimentoVariableInit();
                            if(token.getTipo().equals(";")){
                                proximo_token();
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            proximo_token();
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,.,&,|,/,*,-,+,>,<,=,!",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
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
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"global,local,IDE,true,false,CAD,NRO,(,!",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
            }else if(tokenAux.getLexema().equals(";")){
                proximo_token();
                proximo_token();
            }else{
                proximo_token();
                ErroSintatico erro = new ErroSintatico(token.getLinha(),";,=,[,.",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
                                if(token.getLexema().equals(";")){
                                    proximo_token();
                                }else{
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else if(tokenAux.getLexema().equals("[")){
                                proximo_token();
                                proximo_token();
                                procedimentoArrayUsage();
                                if(token.getLexema().equals(";")){
                                    proximo_token();
                                }else{
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                proximo_token();
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),".,[,;",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }
                            /*else if(tokenAux.getLexema().equals("local") || tokenAux.getLexema().equals("global")){
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
                                                ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                                System.out.println(erro.info());
                                                proximo_token();
                                            }
                                        }else if(token.getLexema().equals(";")){
                                            proximo_token();
                                        }else{
                                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"[,;",token.getLexema());
                                            System.out.println(erro.info());
                                            proximo_token();
                                        }
                                    }
                                }
                            }*/
                        }else if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                            //proximo_token();
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
                                            ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                            System.out.println(erro.info());
                                            proximo_token();
                                        }
                                    }else if(token.getLexema().equals(";")){
                                        proximo_token();
                                    }else{
                                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"[,;",token.getLexema());
                                        System.out.println(erro.info());
                                        proximo_token();
                                    }
                                }
                            }
                        }

                    }/*else if(tokenAux.getLexema().equals("[")){
                        proximo_token();
                        proximo_token();
                        procedimentoArrayUsage();
                        if(token.getLexema().equals("=")){
                            proximo_token();
                            if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                                proximo_token();
                                if(token.getLexema().equals(".")){
                                    proximo_token();
                                    if(token.getTipo().equals("IDE")){
                                        proximo_token();
                                        if(token.getLexema().equals(";")){
                                            proximo_token();
                                        }else{
                                            ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                            System.out.println(erro.info());
                                            proximo_token();
                                        }
                                    }else{
                                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                        System.out.println(erro.info());
                                        proximo_token();
                                    }
                                }else{
                                    ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                                    System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                ErroSintatico erro = new ErroSintatico(token.getLinha(),"local,global",token.getLexema());
                                System.out.println(erro.info());
                                proximo_token();
                            }

                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"=",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }

                    }*/else if(tokenAux.getLexema().equals(".") || tokenAux.getLexema().equals("[")){//chamada de variableUsage em VariableScoope
                        procedimentoVariableUsage();
                    }else{
                        proximo_token();
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"=,[,.",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }

            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }
        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"local,global",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
        }
        /*else if(token.getTipo().equals("IDE")){

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
        }**/
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
                        proximo_token();
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
                                proximo_token();
                            }
                        }else{
                            ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                            System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                ErroSintatico erro = new ErroSintatico(token.getLinha(),"struct",token.getLexema());
                System.out.println(erro.info());
                proximo_token();
            }

        }else{
            ErroSintatico erro = new ErroSintatico(token.getLinha(),"typedef",token.getLexema());
            System.out.println(erro.info());
            proximo_token();
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
            proximo_token();
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

    public boolean primeiroOperadores(Token token1){
        if(token1.getLexema().equals("+") || token1.getLexema().equals("-") || token1.getLexema().equals("*")
                || token1.getLexema().equals("/") || token1.getLexema().equals("++") || token1.getLexema().equals("--")
                    || token1.getLexema().equals("==") || token1.getLexema().equals("!=") || token1.getLexema().equals(">")
                        || token1.getLexema().equals(">=")|| token1.getLexema().equals("<") || token1.getLexema().equals("<=")
                            || token1.getLexema().equals("=") || token1.getLexema().equals("&&") || token1.getLexema().equals("||")
                                || token1.getLexema().equals("!")){
            return true;
        }else{
            return false;
        }
    }
}

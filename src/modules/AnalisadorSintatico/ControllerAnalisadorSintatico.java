package modules.AnalisadorSintatico;

import domain.entities.Token;
import modules.AnalisadorSintatico.usecases.ErroSintatico;
import modules.TabelaSimbolos.usecases.facade.*;
import modules.TabelaSimbolos.usecases.impl.*;

import java.util.ArrayList;
import java.util.List;

public class ControllerAnalisadorSintatico {
    /**Variáveis auxiliares**/
    public Token token;
    public int quantidadeParametrosAux;
    public ArrayList<String> tiposParametros;
    public Token ideAux;
    public String tipoRetorno;
    public ArrayList<Token> argumentosAux;
    public ArrayList<Token> argumentosFuncAux;
    public String tipoConstante;

    public ArrayList<Token> listaTokens;
    public ArrayList<Token> listaTokensAuxilixar;
    public int indiceTokenAtual = 0;
    public Token tokenFimArquivo;
    public ArrayList<ErroSintatico> erros;
    public boolean controleErro;
    public ITabelaSimbolos tabelaDeSimbolos;

    public ArrayList<Token> iniciarLeitura(ArrayList<Token> tokens){
        this.indiceTokenAtual = 0;
        this.quantidadeParametrosAux = 0;
        this.listaTokens = tokens;
        this.listaTokensAuxilixar = new ArrayList<>();
        this.tiposParametros = new ArrayList<>();
        this.argumentosAux = new ArrayList<>();
        this.tabelaDeSimbolos = new TabelaSimbolosImpl();
        this.tokenFimArquivo = new Token("$","EOF",false);
        if(tokens.size() != 0){
            this.token = tokens.get(this.indiceTokenAtual);
        }else{
            this.token = tokenFimArquivo;
        }
        this.tokenFimArquivo.setLinha(00);
        this.listaTokens.add(tokenFimArquivo);
        this.erros = new ArrayList<>();
        this.controleErro = false;

        //Teste
        Token t1 = new Token("IDE","h",false);
        Token t2 = new Token("IDE","j",false);
        Token t3 = new Token("IDE","t",false);
        VariaveisImpl var1 = new VariaveisImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,t1,1);
        VariaveisImpl var2 = new VariaveisImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,t2,1);
        VariaveisImpl var3 = new VariaveisImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,t3,1);
        var1.setTipoVariavel("string");
        var2.setTipoVariavel("int");
        var3.setTipoVariavel("boolean");

        tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, var1);
        tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, var2);
        tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, var3);

        this.init();
        System.out.println("Tamanho tabela: " + this.tabelaDeSimbolos.numeroSimbolos());
        return this.listaTokensAuxilixar;
    }

    public void proximo_token(){
        //System.out.println(indiceTokenAtual);
        if(!controleErro){
            listaTokensAuxilixar.add(token);
        }
        this.indiceTokenAtual++;

        if(this.indiceTokenAtual < listaTokens.size()){
            this.token = listaTokens.get(this.indiceTokenAtual);
            //listaTokensAuxilixar.add(token);
        }else{
            this.listaTokens.add(tokenFimArquivo);
            this.token = listaTokens.get(this.indiceTokenAtual);
            //listaTokensAuxilixar.add(this.token);
        }
        controleErro = false;
    }

    public void configurarErro(Token token,String lexemaEsperado){
        ErroSintatico error = new ErroSintatico(token.getLinha(),lexemaEsperado,token.getLexema());
        Token tokenAuxiliar = new Token(token.getTipo(),token.getLexema(),true);
        tokenAuxiliar.setError(error);
        tokenAuxiliar.setErroSintatico(true);
        listaTokensAuxilixar.add(tokenAuxiliar);
        System.out.println(error.info());
        this.controleErro = true;
    }
    public void init(){
        if(token.getLexema().equals("function") || token.getLexema().equals("procedure") || token.getTipo().equals("var")
                || token.getLexema().equals("const") || token.getLexema().equals("typedef") /*|| token.getLexema().equals("local")
                || token.getLexema().equals("global")*/){

            if(token.getLexema().equals("procedure") ){
                Token tokenAux = listaTokens.get(this.indiceTokenAtual+1);
                if(tokenAux.getLexema().equals("start")){
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
                                    this.configurarErro(token,"}");
                                    proximo_token();
                                }
                            }
                        }else{
                            this.configurarErro(token,"{");
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"start");
                        proximo_token();
                    }
                }else{
                    proximo_token();
                    procedimentoDeclProcedure();
                    init();
                }
            }else{
                simpleStatement();
                init();
            }
        }
    }

    public void start(){
        //Fim do arquivo token.getLexema().equals("}")
        //Token tokenAux = this.listaTokens.get(this.indiceTokenAtual+1); tokenAux.getTipo().equals("EOF")
        //System.out.println("indice atual: " + indiceTokenAtual + " Tamanho: " + listaTokens.size());
        if(indiceTokenAtual == (listaTokens.size()-2)) {

        }else{
            this.program();
            this.start();
        }
    }
    /*public void init(){

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
                            this.configurarErro(token,"}");
                            //ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                            //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                            //token.setError(error);
                            //listaTokensAuxilixar.add(token);
                            //System.out.println(error.info());
                            proximo_token();
                        }
                    }
                }else{
                    this.configurarErro(token,"{");
                    //ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                    //token.setError(error);
                    //listaTokensAuxilixar.add(token);
                    //System.out.println(error.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"start");
                //ErroSintatico error = new ErroSintatico(token.getLinha(),"start",token.getLexema());
                //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
                //token.setError(error);
                //listaTokensAuxilixar.add(token);
                //System.out.println(error.info());
                proximo_token();
            }
        }else {
            this.configurarErro(token,"procedure");
            //ErroSintatico error = new ErroSintatico(token.getLinha(),"procedure",token.getLexema());
            //Token token = new Token(this.token.getTipo(), this.token.getLexema(),true);
            //token.setError(error);
            //listaTokensAuxilixar.add(token);
            //System.out.println(error.info());
            proximo_token();
        }
    }

    public void start(){
        //Fim do arquivo token.getLexema().equals("}")
        //Token tokenAux = this.listaTokens.get(this.indiceTokenAtual+1); tokenAux.getTipo().equals("EOF")
        //System.out.println("indice atual: " + indiceTokenAtual + " Tamanho: " + listaTokens.size());
        if(indiceTokenAtual == (listaTokens.size()-2)) {

        }else{
            this.program();
            this.start();
        }
    }*/

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
                this.configurarErro(token,"}");
                proximo_token();
            }
        }else if(primeiroSimpleStatement().contains(token.getLexema()) || this.token.getTipo().equals("IDE")){
            simpleStatement();
        }else{
            this.configurarErro(token,"{,IDE,procedure,function,if,while,read,print,typedef,var,const,local,global");
            proximo_token();
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
                procedimentoCallFunc("0");
                if(token.getLexema().equals(";")){
                    proximo_token();
                }else{
                    this.configurarErro(token,";");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }else if(tokenAux.getLexema().equals(".") || tokenAux.getLexema().equals("[")
                    || tokenAux.getLexema().equals("=") || tokenAux.getLexema().equals(";")){
                procedimentoVariableUsage();
            }else{
                //Talvez não tenha erro
                proximo_token();
                this.configurarErro(token,"(,.,[,=,;");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,local,global,.,[,=,;",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else if(token.getLexema().equals("local") || token.getLexema().equals("global")){
            procedimentoVariableScope();
        }else{
            this.configurarErro(token,"read,print,while,if,function,procedure,typedef,var,const,IDE");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"read,print,while,if,function,procedure,typedef,var,const,IDE",token.getLexema());
            //System.out.println(erro.info());
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
                        this.configurarErro(token,"then");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"then",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }
                else{
                    this.configurarErro(token,")");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"true,false,NRO,CAD,(,!,IDE");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD,(,!,IDE",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }

        }else{
            this.configurarErro(token,"(");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            //System.out.println(erro.info());
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
                        this.configurarErro(token,"{");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }
                else{
                    this.configurarErro(token,")");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"true,false,NRO,CAD,(,!,IDE");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD,(,!,IDE",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"(");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            //System.out.println(erro.info());
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
            this.configurarErro(token,"(,true,false,NRO,CAD,IDE");
            proximo_token();
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
                    this.configurarErro(token,";");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }
            else{
                this.configurarErro(token,")");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"(");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            //System.out.println(erro.info());
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
            this.configurarErro(token,"IDE");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(erro.info());
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
            this.configurarErro(token,",");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),",",token.getLexema());
            //System.out.println(erro.info());
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
                    this.configurarErro(token,";");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }
            else{
                this.configurarErro(token,")");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"(");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
            //System.out.println(erro.info());
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
            this.configurarErro(token,"IDE,CAD");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,CAD",token.getLexema());
            //System.out.println(erro.info());
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
            this.configurarErro(token,",");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),",",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoDeclProcedure(){

        Token identificadorAux;
        if(token.getTipo().equals("IDE")){
            identificadorAux = token;
            proximo_token();
            if(token.getLexema().equals("(")){
                proximo_token();
                procedimentoParams();
                /**Adiciona o identificador na tabela de simbolos**/
                if(!verificarDuplicidade(identificadorAux,"procedimento",this.quantidadeParametrosAux,this.tiposParametros,this.tipoRetorno)) {
                    ProcedimentoImpl procedimento = new ProcedimentoImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,1);
                    procedimento.setQuantidadeParametros(this.quantidadeParametrosAux);
                    procedimento.setTiposParametros(this.tiposParametros);

                    /*IdentificadorImpl identificador = new IdentificadorImpl();
                    identificador.configurarIdentificador(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,1);
                    procedimento.setIdentificador(identificador);*/

                    tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, procedimento);
                }else{
                    System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Já existe um procedimento com o nome " + identificadorAux.getLexema());
                }
                if(token.getLexema().equals(")")){
                    proximo_token();
                    procedimentoBlockProc();
                }else{
                    this.configurarErro(token,")");
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"(");
                proximo_token();
            }
        }else{
            this.configurarErro(token,"IDE");
            proximo_token();
        }
        this.quantidadeParametrosAux = 0;
        this.tiposParametros.clear();
    }

    public void procedimentoBlockProc(){
        if(token.getLexema().equals("{")){
            proximo_token();
            statementsList();
            if(token.getLexema().equals("}")){
                proximo_token();
            }else{
                this.configurarErro(token,"}");
                proximo_token();
            }
        }else{
            this.configurarErro(token,"{");
            proximo_token();
        }
    }

    public void procedimentoDeclFunction(){
        Token identificadorAux;
        if(primeiroType(token)){
            this.tipoRetorno = token.getLexema();
            proximo_token();
            if(token.getTipo().equals("IDE")){
                identificadorAux = token;
                proximo_token();
                if(token.getLexema().equals("(")){
                    proximo_token();
                    procedimentoParams();
                    /**Adiciona o identificador na tabela de simbolos**/
                    if(!verificarDuplicidade(identificadorAux,"função",this.quantidadeParametrosAux,this.tiposParametros,this.tipoRetorno)) {
                        FuncaoImpl funcao = new FuncaoImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,1);
                        funcao.setQuantidadeParametros(this.quantidadeParametrosAux);
                        funcao.setTiposParametros(this.tiposParametros);
                        funcao.setTipoRetorno(this.tipoRetorno);
                    /*IdentificadorImpl identificador = new IdentificadorImpl();
                    identificador.configurarIdentificador(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,1);
                    procedimento.setIdentificador(identificador);*/

                        tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, funcao);
                    }else{
                        System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Já existe uma função com o nome " + identificadorAux.getLexema());
                    }
                    if(token.getLexema().equals(")")){
                        /**
                        --procurar identificador na tabela de simbolos,indicar que ele foi declarado e que é uma
                        função, colocar a quantidade de parametros e os seus tipos e o tipo do retorno.
                        */
                        proximo_token();
                        procedimentoBlockFunc();
                    }else{
                        this.configurarErro(token,")");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),")",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    this.configurarErro(token,"(");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"IDE");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"boolean,int,real,string");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"boolean,int,real,string",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
        this.quantidadeParametrosAux = 0;
        this.tiposParametros.clear();
    }

    public void procedimentoBlockFunc(){
        if(token.getLexema().equals("{")){
            proximo_token();
            statementsList();
            procedimentoReturn();
            if(token.getLexema().equals("}")){
                proximo_token();
            }else{
                this.configurarErro(token,"}");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"{");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoReturn(){
        /**
         * Verificar se o retorno é igual ao que foi declarado na função.
         * **/
        if(token.getLexema().equals("return")){
            proximo_token();
            if(token.getTipo().equals("IDE")){
                Token tokenAux = listaTokens.get(indiceTokenAtual + 1);
                if(tokenAux.getLexema().equals("(")){
                    procedimentoCallFunc("função");
                }else{
                    proximo_token();
                }
            }else if(token.getTipo().equals("CAD")){
                proximo_token();
            }else if(token.getTipo().equals("NRO")){
                proximo_token();
            }else if(token.getLexema().equals("true") || token.getLexema().equals("false")){
                proximo_token();
            }else if(!token.getLexema().equals(";")){
                this.configurarErro(token,";,IDE,CAD,NRO");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
            if(token.getLexema().equals(";")){
                proximo_token();
            }
        }else{
            this.configurarErro(token,"return");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"return",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoParams(){
        this.quantidadeParametrosAux++;
        if(primeiroType(token)){
            this.tiposParametros.add(token.getLexema());
            proximo_token();
            procedimentoParam();
            if(token.getLexema().equals(",")){
                proximo_token();
                procedimentoParams();
            }
        }else if(token.getLexema().equals(")")){

        }else{
            this.configurarErro(token,"boolean,int,real,string");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"boolean,int,real,string",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoParam(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
        }else{
            this.configurarErro(token,"IDE");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoCallFunc(String tipo){
        /**verificar se já foi declarada**/
        if(token.getTipo().equals("IDE")){
            Token identificadorAux = token;
            proximo_token();
            if(token.getLexema().equals("(")){
                proximo_token();
                /**Verificar se a quantidade de parametros está correto**/
                procedimentoArgs();
                if(verificarProcFun(identificadorAux,tipo)){
                    verificarArgumentos(identificadorAux,this.argumentosAux,tipo);
                }else{
                    System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() +
                            " o identificador " + identificadorAux.getLexema() + " que foi utilizado não é um(a) " +
                            "procedimento/função ou não foi declarado.");
                }

                if(token.getLexema().equals(")")){
                    proximo_token();
                    /* Confere se o identificador é uma função/procedimento, se já foi declarado e se os parametros
                       estão corretos.
                    * */
                }else{
                    this.configurarErro(token,")");
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"(");
                proximo_token();
            }
        }else{
            this.configurarErro(token,"IDE");
            proximo_token();
        }
        this.argumentosAux.clear();
    }

    public void procedimentoArgs(){
        if(token.getTipo().equals("IDE") || token.getTipo().equals("NRO") || token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("CAD")){
            procedimentoArg();
            if(token.getLexema().equals(",")){
                proximo_token();
                procedimentoArgs();
            }
        }
    }

    public void procedimentoArg(){
        if(token.getTipo().equals("IDE")){
            argumentosAux.add(token);
            Token tokenAux = listaTokens.get(indiceTokenAtual+1);
            if(tokenAux.getLexema().equals("(")){
                procedimentoCallFunc("função");
            }else{
                proximo_token();
            }
        }else if(token.getTipo().equals("NRO") || token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("CAD")){
            argumentosAux.add(token);
            proximo_token();
        }else{
            this.configurarErro(token,"IDE,NRO,true,false,CAD");
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
                        this.configurarErro(token,"}");
                        //ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        //System.out.println(error.info());
                        proximo_token();
                    }
                }else if(token.getLexema().equals("}")){
                   proximo_token();
                }else{
                    this.configurarErro(token,"int,real,string,boolean,}");
                    //ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
                    //System.out.println(error.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"}");
                //ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                //System.out.println(error.info());
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
                        this.configurarErro(token,"}");
                        //ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        //System.out.println(error.info());
                        proximo_token();
                    }
                }else{
                    this.configurarErro(token,"int,real,string,boolean");
                    //ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
                    //System.out.println(error.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"{");
                //ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                //System.out.println(error.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"var,const");
            //ErroSintatico error = new ErroSintatico(token.getLinha(),"var,const",token.getLexema());
            //System.out.println(error.info());
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
                this.configurarErro(token,";");
                //ErroSintatico error = new ErroSintatico(token.getLinha(),";",token.getLexema());
                //System.out.println(error.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"int,real,string,boolean");
            //ErroSintatico error = new ErroSintatico(token.getLinha(),"int,real,string,boolean",token.getLexema());
            //System.out.println(error.info());
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
            this.configurarErro(token,"IDE");
            //ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(error.info());
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
                                this.configurarErro(token,"}");
                                //ErroSintatico error = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                                //System.out.println(error.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,"true,false,NRO,CAD");
                            //ErroSintatico error = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD",token.getLexema());
                            //System.out.println(error.info());
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"{");
                        //ErroSintatico error = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                        //System.out.println(error.info());
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
                        procedimentoCallFunc("função");
                    }else if(primeiroOperadores(tokenAux)){
                        procedimentoVariableInit();
                    }else if(tokenAux.getLexema().equals(";")){
                        proximo_token();
                        //proximo_token();
                    }else{
                        proximo_token();
                        this.configurarErro(token,"(,&&,||,/,*,-,--,+,++,>,>=,<,<=,==,!,!=,=,.,");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,.,&,|,/,*,-,+,>,<,=,!",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }else if(token.getLexema().equals("local") || token.getLexema().equals("global")){
                    proximo_token();
                    if(token.getLexema().equals(".")){
                        proximo_token();
                        if(token.getTipo().equals("IDE")){
                            proximo_token();
                        }else{
                            this.configurarErro(token,"IDE");
                            //ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                            //System.out.println(error.info());
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,".");
                        //ErroSintatico error = new ErroSintatico(token.getLinha(),".",token.getLexema());
                        //System.out.println(error.info());
                        proximo_token();
                    }
                }else if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                        || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!")){
                    procedimentoVariableInit();
                }else{
                    this.configurarErro(token,"IDE,local,global,true,false,NRO,CAD,(,!");
                    //ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE,local,global,true,false,NRO,CAD,(,!",token.getLexema());
                    //System.out.println(error.info());
                    proximo_token();
                }
            }
        }else{
            this.configurarErro(token,"IDE");
            //ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(error.info());
            proximo_token();
        }
    }

    public void procedimentoArrayUsage(){

        if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
            //verificarIndice(token,"variavel");
            proximo_token();
            if(token.getLexema().equals("]")){
                proximo_token();
                if(token.getLexema().equals("[")){
                    proximo_token();
                    if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
                        //verificarIndice(token,"variavel");
                        proximo_token();
                        if(token.getLexema().equals("]")){
                            /**
                             * Verificar se é uma matriz, se já foi declarado e se o elemento que está representando o indice está
                             * correto.
                             * */
                            proximo_token();
                        }else{
                            this.configurarErro(token,"]");
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"IDE,true,false,NRO");
                        proximo_token();
                    }
                }
                /**
                 * Verificar se é um array, se já foi declarado e se o elemento que está representando o indice está
                 * correto. Colocar uma forma que se entrou em matriz, não verificar essas condições.
                * */
            }else{
                this.configurarErro(token,"]");
                proximo_token();
            }
        }else{
            this.configurarErro(token,"IDE,true,false,NRO");
            proximo_token();
        }
    }

    public void procedimentoVarArgs(){
        if(token.getLexema().equals("true") || token.getLexema().equals("false")
                || token.getTipo().equals("NRO") || token.getTipo().equals("CAD")){
            proximo_token();
            if(token.getLexema().equals(",")){
                proximo_token();
                procedimentoVarArgs();
            }

        }else{
            this.configurarErro(token,"IDE,true,false,NRO,CAD");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,true,false,NRO,CAD",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    /*public void procedimentoVarArg(){

    }
    */

    public void procedimentoTypedConst(){
        if(primeiroType(token)){
            tipoConstante = token.getLexema();
            proximo_token();
            procedimentoConstants();
            if(token.getLexema().equals(";")){
                proximo_token();
                if(primeiroType(token)){
                    procedimentoTypedConst();
                }
            }else{
                this.configurarErro(token,";");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"boolean,string,int,real");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"int,real,boolean,string",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }

    }

    public void procedimentoConstants(){
        if(token.getTipo().equals("IDE")){
            Token identificadorAux = token;
            proximo_token();
            if(token.getLexema().equals("=")){
                proximo_token();
                if(token.getLexema().equals("true") || token.getLexema().equals("false")
                        || token.getTipo().equals("NRO") || token.getTipo().equals("CAD")){
                    if(compatibilidadeTipos(this.tipoConstante,token)){
                        int controle = verificarConst(identificadorAux);
                        if(controle == 0){
                            ConstanteImpl constante = new ConstanteImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,1);
                            constante.setTipoConstante(this.tipoConstante);
                            tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, constante);
                        }else if(controle == -1){
                            System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Já existe uma constante com o nome " + identificadorAux.getLexema());
                        }else{
                            System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Já existe uma variável com o nome " + identificadorAux.getLexema());
                        }
                    }else{
                        System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Valor atribuido a constante " + identificadorAux.getLexema() +
                                " não é um compativel com o tipo declarado na constante");
                    }
                    proximo_token();
                    if(token.getLexema().equals(",")){
                        proximo_token();
                        procedimentoConstants();
                    }
                }else{
                    this.configurarErro(token,"IDE,true,false,NRO,CAD");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,true,false,NRO,CAD",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"=");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"=",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"IDE");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
        }
    }

    public void procedimentoStructUsage(){
        if(token.getTipo().equals("IDE")){
            proximo_token();
            if(token.getLexema().equals(".")){
                proximo_token();
                if(token.getTipo().equals("IDE")){
                    /**
                     * Verificar se é uma struct, se já foi declarado e se o elemento que o mesmo está acessando faz
                     * parte da struct.
                     * */
                    proximo_token();
                }else{
                    this.configurarErro(token,"IDE");
                    proximo_token();
                }
            }else{
                this.configurarErro(token,".");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"IDE");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(erro.info());
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
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("[")){
                            proximo_token();
                            proximo_token();
                            procedimentoArrayUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("(")){
                            procedimentoCallFunc("função");
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(primeiroOperadores(tokenAux)){
                            procedimentoVariableInit();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals(";")){
                            proximo_token();
                            proximo_token();
                        }else{
                            proximo_token();
                            this.configurarErro(token,"(,&&,||,/,*,-,--,+,++,>,>=,<,<=,==,!,!=,=,.,;");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,.,&,|,/,*,-,+,>,<,=,!",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                            || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!")){
                        procedimentoVariableInit();
                        if(token.getLexema().equals(";")){
                            proximo_token();
                        }else{
                            this.configurarErro(token,";");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                            //System.out.println(erro.info());
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
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                this.configurarErro(token,"IDE");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,".");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"IDE,true,false,NRO,CAD,(,!,local,global");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE,true,false,NRO,CAD,(,!,local,global",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    this.configurarErro(token,";,=");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),"=,;",token.getLexema());
                    //System.out.println(erro.info());
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
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("[")){
                            proximo_token();
                            proximo_token();
                            procedimentoArrayUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals(";")){
                            proximo_token();
                            proximo_token();
                        }else{
                            proximo_token();
                            this.configurarErro(token,".,[");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),".,[",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else if(token.getTipo().equals("NRO") || token.getTipo().equals("IDE") || token.getLexema().equals("true") || token.getLexema().equals("false")){
                        proximo_token();
                        if(token.getLexema().equals(";")){
                            proximo_token();
                        }else{
                            this.configurarErro(token,";");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                            //System.out.println(erro.info());
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
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                this.configurarErro(token,"}");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,"true,false,NRO,CAD");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"true,false,NRO,CAD",token.getLexema());
                            //System.out.println(erro.info());
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
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                this.configurarErro(token,"IDE");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,".");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"local,global,{,IDE");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"local,global,{,IDE",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    this.configurarErro(token,";,=");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";,=",token.getLexema());
                    //System.out.println(erro.info());
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
                            this.configurarErro(token,";");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else if(token.getTipo().equals("IDE")){
                        tokenAux = listaTokens.get(indiceTokenAtual + 1);
                        if(tokenAux.getLexema().equals(".")){
                            procedimentoStructUsage();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                proximo_token();
                            }
                        }else if(tokenAux.getLexema().equals("(")){
                            procedimentoCallFunc("função");
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else if(primeiroOperadores(tokenAux)){
                            procedimentoVariableInit();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            proximo_token();
                            this.configurarErro(token,"(,&&,||,/,*,-,--,+,++,>,>=,<,<=,==,!,!=,=,.");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"(,.,&,|,/,*,-,+,>,<,=,!",token.getLexema());
                            //System.out.println(erro.info());
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
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                this.configurarErro(token,"IDE");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,".");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"global,local,IDE,true,false,CAD,NRO,(,!");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"global,local,IDE,true,false,CAD,NRO,(,!",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
            }else if(tokenAux.getLexema().equals(";")){
                proximo_token();
                proximo_token();
            }else{
                proximo_token();
                this.configurarErro(token,";,=,[,.");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";,=,[,.",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"IDE");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
            //System.out.println(erro.info());
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
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else if(tokenAux.getLexema().equals("[")){
                                proximo_token();
                                proximo_token();
                                procedimentoArrayUsage();
                                if(token.getLexema().equals(";")){
                                    proximo_token();
                                }else{
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else if(tokenAux.getLexema().equals("(")){
                                procedimentoCallFunc("função");
                            }else if(primeiroOperadores(tokenAux)){
                                procedimentoVariableInit();
                                if(token.getLexema().equals(";")){
                                    proximo_token();
                                }else{
                                    this.configurarErro(token,";");
                                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                    //System.out.println(erro.info());
                                    proximo_token();
                                }
                            }else{
                                proximo_token();
                                this.configurarErro(token,";,[,.,(,&&,||,/,*,-,--,+,++,>,>=,<,<=,==,!,!=,=");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),".,[,;",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
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
                                            this.configurarErro(token,";");
                                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                            //System.out.println(erro.info());
                                            proximo_token();
                                        }
                                    }else if(token.getLexema().equals(";")){
                                        proximo_token();
                                    }else{
                                        this.configurarErro(token,";,[");
                                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"[,;",token.getLexema());
                                        //System.out.println(erro.info());
                                        proximo_token();
                                    }
                                }
                            }
                        }else if(token.getLexema().equals("true") || token.getLexema().equals("false") || token.getTipo().equals("NRO")
                                || token.getTipo().equals("CAD") || token.getLexema().equals("(") || token.getLexema().equals("!")){
                            procedimentoVariableInit();
                            if(token.getLexema().equals(";")){
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,"IDE,local,global,true,false,NRO,CAD,(,!");
                            //ErroSintatico error = new ErroSintatico(token.getLinha(),"IDE,local,global,true,false,NRO,CAD,(,!",token.getLexema());
                            //System.out.println(error.info());
                            proximo_token();
                        }

                    }else if(tokenAux.getLexema().equals(".") || tokenAux.getLexema().equals("[")){//chamada de variableUsage em VariableScoope
                        procedimentoVariableUsage();
                    }else if(tokenAux.getLexema().equals(";")){
                        proximo_token();
                        proximo_token();
                    }else{
                        proximo_token();
                        this.configurarErro(token,"=,[,.");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"=,[,.",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    this.configurarErro(token,"IDE");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }

            }else{
                this.configurarErro(token,".");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),".",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }
        }else{
            this.configurarErro(token,"local,global");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"local,global",token.getLexema());
            //System.out.println(erro.info());
            proximo_token();
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
                        /**
                         * verifica se o identificador é uma struct, e adicona ela como pai dessa struct
                         * */
                    }else{
                        this.configurarErro(token,"IDE");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                        //System.out.println(erro.info());
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
                                /**
                                 * coloca que o identificador é uma struct, e adicona os seus atributos e indica que
                                 * já foi declarado.
                                 * */
                                proximo_token();
                            }else{
                                this.configurarErro(token,";");
                                //ErroSintatico erro = new ErroSintatico(token.getLinha(),";",token.getLexema());
                                //System.out.println(erro.info());
                                proximo_token();
                            }
                        }else{
                            this.configurarErro(token,"IDE");
                            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"IDE",token.getLexema());
                            //System.out.println(erro.info());
                            proximo_token();
                        }
                    }else{
                        this.configurarErro(token,"}");
                        //ErroSintatico erro = new ErroSintatico(token.getLinha(),"}",token.getLexema());
                        //System.out.println(erro.info());
                        proximo_token();
                    }
                }else{
                    this.configurarErro(token,"{");
                    //ErroSintatico erro = new ErroSintatico(token.getLinha(),"{",token.getLexema());
                    //System.out.println(erro.info());
                    proximo_token();
                }
            }else{
                this.configurarErro(token,"struct");
                //ErroSintatico erro = new ErroSintatico(token.getLinha(),"struct",token.getLexema());
                //System.out.println(erro.info());
                proximo_token();
            }

        }else{
            this.configurarErro(token,"typedef");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"typedef",token.getLexema());
            //System.out.println(erro.info());
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
            this.configurarErro(token,"var,const");
            //ErroSintatico erro = new ErroSintatico(token.getLinha(),"var,const",token.getLexema());
            //System.out.println(erro.info());
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
        listaPrimeiro.add("local");
        listaPrimeiro.add("global");
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

    /************************************************* Analisador Semantico ***************************************************************/

    public void verificarIndice(Token token,String tipo){
        int controle = 0;
        if(token.getTipo().equals("IDE")){
            /** Buscar na tabela o tipo do identificador **/
            if(verificarDeclaracao(token,tipo)){
                IVariaveis aux = (IVariaveis) this.tabelaDeSimbolos.getSimbolo(token,tipo);
                if(aux.getTipoVariavel().equals("inteiro")){

                }else{
                    System.out.println(token.getLinha() + " Tipo de indice inválido");
                }
            }
        }else if(token.getTipo().equals("NRO")){
            /** Criar no token um atributo para verificar se é inteiro**/
            if(token.getLexema().contains(".")){
                System.out.println(token.getLinha() + " Tipo de indice inválido");
            }else{

            }
        }
        /*
        if(controle == 0){
            return true;
        }else{
            return false;
        }
        */
    }

    public boolean verificarDeclaracao(Token token, String tipo){
        String tipoAux = " ";
        int controle = 0;
        IIdentificador aux = this.tabelaDeSimbolos.getSimbolo(token, tipo);
        if(aux == null){
            System.out.println(token.getLinha() + tipoAux + "não declarado(a)");
            controle = 1;
        }else{
            controle = 0;
        }

        if(controle == 0){
            return true;
        }else{
            return false;
        }
    }

    public void verificarTipoVariavel(Token token ,String modeloVariavel){

        if(verificarDeclaracao(token,"variavel")){
            IVariaveis aux = (IVariaveis) this.tabelaDeSimbolos.getSimbolo(token,"variavel");
            if(aux.getModeloVariavel().equals(modeloVariavel)){

            }else{
                System.out.println("Erro Semântivo: " + token.getLinha() + "variável utilizada não é: " + modeloVariavel);
            }
        }
    }

    public boolean verificarDuplicidade(Token token,String tipo, int quantidadeParametros, ArrayList<String> tipos, String retorno){
        int controle = 0;
        if(tipo.equals("procedimento")){
            IIdentificador aux = this.tabelaDeSimbolos.getSimboloL(token,"procedimento");
            if(aux != null){
                IProcedimento auxProc = (IProcedimento)aux;
                if(auxProc.getQuantidadeParametros() == quantidadeParametros){
                    for(int i = 0; i < quantidadeParametros; i++){
                        if(!auxProc.getTiposParametros().get(i).equals(tipos.get(i))){
                            controle = 1;
                            break;
                        }
                    }
                    if(controle == 1){
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            IIdentificador aux = this.tabelaDeSimbolos.getSimboloL(token,"função");
            if(aux != null){
                IFuncao auxFun = (IFuncao) aux;
                //System.out.println("Quant parametros: "+auxFun.get+"--------"+quantidadeParametros);
                if(auxFun.getQuantidadeParametros() == quantidadeParametros){
                    if(auxFun.getTipoRetorno().equals(retorno)) {
                        for (int i = 0; i < quantidadeParametros; i++) {
                            if (!auxFun.getTiposParametros().get(i).equals(tipos.get(i))) {
                                controle = 1;
                                break;
                            }
                        }
                        if (controle == 1) {
                            return false;
                        } else {
                            return true;
                        }
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public void verificarArgumentos(Token identificador, ArrayList<Token> argumentos, String tipo){

        int controle = 0;
        int controleTam = 0;
        int controleAux = 0;
        ArrayList<String> auxiliar = new ArrayList<>();
        ArrayList<IIdentificador> aux = this.tabelaDeSimbolos.getSimbolos(identificador,"procedimento");
        ArrayList<IIdentificador> aux2 = this.tabelaDeSimbolos.getSimbolos(identificador,"função");
        IIdentificador aux3;

        for(Token token: argumentos){
            if(token.getTipo().equals("IDE")){
                aux3 = this.tabelaDeSimbolos.getSimboloL(token,"variavel");
                if(aux3 != null){
                    auxiliar.add(((IVariaveis)aux3).getTipoVariavel());
                }else{
                    controleAux = 1;
                    System.out.println("Erro Semântivo: "+ " Linha" + token.getLinha() + " a variavel " + token.getLexema() + " não foi declarada");
                }
            }else if(token.getTipo().equals("NRO")){
                if(token.getLexema().contains(".")){
                    auxiliar.add("real");
                }else{
                    auxiliar.add("int");
                }
            }else if(token.getTipo().equals("CAD")){
                auxiliar.add("string");
            }else if(token.getLexema().equals("true")){
                auxiliar.add("boolean");
            }else{
                auxiliar.add("boolean");
            }
        }

        /*for(Token token: argumentos){
            aux3 = this.tabelaDeSimbolos.getSimboloL(token,"variavel");
            if(aux3 != null){
                auxiliar.add(((IVariaveis)aux3));
            }else{
                System.out.println("Erro Semântivo: " + identificador.getLinha() + "Um dos argumentos utilizados não foi declarado");
            }
        }*/

        if(controleAux == 0){
            if(tipo.equals("0")){
                if(aux.size() == 0){
                    tipo = "função";
                }else{
                    tipo = "procedimento";
                }
            }

            if(tipo.equals("procedimento")){

                for(IIdentificador auxI:aux){
                    IProcedimento auxProc = (IProcedimento)auxI;
                    if(auxProc.getQuantidadeParametros() == argumentos.size()){
                        controle = 0;
                        for (int i = 0; i < argumentos.size(); i++) {
                            if (auxProc.getTiposParametros().get(i).equals(auxiliar.get(i))) {
                                controle = 1;
                                break;
                            }else{
                                controle = 2;
                                break;
                            }
                        }
                        controleTam = 1;
                    }
                }
            }else if(tipo.equals("função")){

                for(IIdentificador auxI:aux2){
                    IFuncao auxProc = (IFuncao) auxI;
                    if(auxProc.getQuantidadeParametros() == argumentos.size()){
                        controle = 0;
                        for (int i = 0; i < argumentos.size(); i++) {
                            if (auxProc.getTiposParametros().get(i).equals(auxiliar.get(i))) {
                                controle = 1;
                                break;
                            }else{
                                controle = 2;
                                break;
                            }
                        }
                        controleTam = 1;
                    }
                }
            }

            if(controleTam == 0){
                System.out.println("Erro Semântivo: " + identificador.getLinha() + " Número de argumentos incorreto");
            }else if(controle == 2){
                System.out.println("Erro Semântivo: " + identificador.getLinha() + " Tipo de argumento incorreto: algum dos argumentos da chamada de função está com o tipo incorreto");
            }
        }
    }

    public boolean verificarProcFun(Token identificador, String tipo){
        /**Se chegar 0 no parametro tipo, significa que tanto faz o tipo**/
        IIdentificador aux = null;
        if(tipo == "procedimento"){
            aux = this.tabelaDeSimbolos.getSimboloL(identificador,tipo);
        }else if(tipo == "função"){
            aux = this.tabelaDeSimbolos.getSimboloL(identificador,tipo);
        }else{
            aux = this.tabelaDeSimbolos.getSimboloL(identificador,"procedimento");
            if(aux == null){
                aux = this.tabelaDeSimbolos.getSimboloL(identificador,"função");
            }
        }

        if(aux == null){
            return false;
        }else{
            return true;
        }
    }

    public int verificarConst(Token identificador){

        IIdentificador aux = null;
        int controle = -1;
        aux = this.tabelaDeSimbolos.getSimboloL(identificador,"constante");
        if(aux == null){
            controle = 0;
            aux = this.tabelaDeSimbolos.getSimboloL(identificador,"variavel");
            if(aux != null){
                controle = 1;
            }
        }

        return controle;
    }

    public boolean compatibilidadeTipos(String tipo, Token atribuicao){
        if(tipo.equals("int") && atribuicao.getTipo().equals("NRO")){
            if(atribuicao.getLexema().contains(".")){
                return false;
            }else{
                return true;
            }
        }else if(tipo.equals("real") && atribuicao.getTipo().equals("NRO")){
            if(atribuicao.getLexema().contains(".")){
                return true;
            }else{
                return false;
            }
        }else if(tipo.equals("string") && atribuicao.getTipo().equals("CAD")){
            return true;
        }else if((tipo.equals("boolean") && atribuicao.getLexema().equals("true")) || (tipo.equals("boolean") && atribuicao.getLexema().equals("false"))){
            return true;
        }else{
            return false;
        }
    }

}

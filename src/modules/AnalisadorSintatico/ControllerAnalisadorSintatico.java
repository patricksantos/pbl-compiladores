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
    public Token variavelReceptor;
    public String variavelReceptorTipo;
    public boolean controleExpressãoBooleana;

    public ArrayList<Token> listaTokens;
    public ArrayList<Token> listaTokensAuxilixar;
    public int indiceTokenAtual = 0;
    public Token tokenFimArquivo;
    public ArrayList<ErroSintatico> erros;
    public boolean controleErro;
    public ITabelaSimbolos tabelaDeSimbolos;
    public int escopo = -1;
    public boolean escopoGlobal = true;

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
        var1.setModeloVariavel("variavel");
        var2.setModeloVariavel("variavel");
        var3.setModeloVariavel("variavel");

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
                        this.escopoGlobal = false;
                        this.escopo++;
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
        this.escopoGlobal = true;
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
                procedimentoExpression("boolean");
                if(!controleExpressãoBooleana){
                    System.out.println("Erro Semântico: "+ " Linha" + this.variavelReceptor.getLinha() + " a expressão deve resultar em um tipo booleano");
                }
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
                procedimentoExpression("boolean");
                if(!controleExpressãoBooleana){
                    System.out.println("Erro Semântico: "+ " Linha" + this.variavelReceptor.getLinha() + " a expressão deve resultar em um tipo booleano");
                }
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

    public void procedimentoExpression(String tipo){
        OrExpression(tipo);
    }

    public void OrExpression(String tipo){
        AndExpression(tipo);
        if(token.getLexema().equals("||") && token.getTipo().equals("LOG")){
            if(!tipo.equals("boolean") && (!tipo.equals("erro"))){
                System.out.println("Erro Semântico: "+ " Linha" + this.token.getLinha() + " o operador " + this.token.getLexema() + "só pode ser utilizado em tipos booleanos");
            }
            proximo_token();
            OrExpression(tipo);
        }
    }

    public void AndExpression(String tipo){
        EqualityExpression(tipo);
        if(token.getLexema().equals("&&") && token.getTipo().equals("LOG")){
            if(!tipo.equals("boolean") && (!tipo.equals("erro"))){
                System.out.println("Erro Semântico: "+ " Linha" + this.token.getLinha() + " o operador " + this.token.getLexema() + "só pode ser utilizado em tipos booleanos");
            }
            proximo_token();
            AndExpression(tipo);
        }
    }

    public void EqualityExpression(String tipo){
        CompareExpression(tipo);
        if(token.getLexema().equals("==") || token.getLexema().equals("!=") && token.getTipo().equals("REL")){
            if(!tipo.equals("boolean") && (!tipo.equals("erro"))){
                System.out.println("Erro Semântico: "+ " Linha" + this.token.getLinha() + " o operador " + this.token.getLexema() + "só pode ser utilizado em tipos booleanos");
            }
            proximo_token();
            EqualityExpression(tipo);
        }
    }

    public void CompareExpression(String tipo){
        AddExpression(tipo);
        if(token.getLexema().equals("<") || token.getLexema().equals(">") || token.getLexema().equals("<=") || token.getLexema().equals(">=")){
            if(!tipo.equals("boolean")&& (!tipo.equals("erro"))){
                System.out.println("Erro Semântico: "+ " Linha" + this.token.getLinha() + " o operador " + this.token.getLexema() + "só pode ser utilizado em tipos booleanos");
            }
            proximo_token();
            CompareExpression(tipo);
        }
    }

    public void AddExpression(String tipo){
        MultiplicationExpression(tipo);
        if(token.getLexema().equals("+") || token.getLexema().equals("-")){
            proximo_token();
            AddExpression(tipo);
        }
    }

    public void MultiplicationExpression(String tipo){
        UnaryExpression(tipo);
        if(token.getLexema().equals("*") || token.getLexema().equals("/")){
            proximo_token();
            MultiplicationExpression(tipo);
        }
    }

    public void UnaryExpression(String tipo){
        if(token.getLexema().equals("!")){
            if((!tipo.equals("boolean")) && (!tipo.equals("erro"))){
                System.out.println("Erro Semântico: "+ " Linha" + this.token.getLinha() + " o operador " + this.token.getLexema() + "só pode ser utilizado em tipos booleanos");
            }
            proximo_token();
            UnaryExpression(tipo);
        }else{
            ObjectExpression(tipo);
        }
    }

    public void ObjectExpression(String tipo){
        MethodExpression(tipo);
        /*if(token.getLexema().equals("true") || token.getLexema().equals("false")){
            MethodExpression();
            //! <PrimaryArrayCreationExpression> ::= <>!Empty
        }else{
            proximo_token();
            System.out.println("ERRO");
        }*/
    }
    public void MethodExpression(String tipo){
        PrimaryExpression(tipo);
    }

    public void PrimaryExpression(String tipo){
        boolean resultado = false;
        if(token.getLexema().equals("true") || token.getLexema().equals("false") ||
                token.getTipo().equals("NRO") || token.getTipo().equals("CAD") || token.getTipo().equals("IDE"))
        {
            if(!tipo.equals("erro")){
                if(token.getTipo().equals("IDE")){
                    IIdentificador aux3 = filtrarVariaveis(token,"variavel");
                    if(aux3 != null){
                        if(((IVariaveis)aux3).getInicializado()){
                            if(((IVariaveis)aux3).getTipoVariavel().equals(tipo)){
                                resultado = true;
                            }
                        }else{
                            System.out.println("Erro Semântico: "+ " Linha" + token.getLinha() + " a variavel " + token.getLexema() + " não foi inicializada");
                        }
                    }else{
                        aux3 = this.tabelaDeSimbolos.getSimboloL(token,"constante");
                        if(aux3 != null) {
                            if(((IConstante) aux3).getTipoConstante().equals(tipo)){
                                resultado = true;
                            }
                        }else{
                            System.out.println("Erro Semântico: "+ " Linha" + token.getLinha() + " a variavel/constante " + token.getLexema() + " não foi declarada");
                        }

                    }
                }else{
                    resultado = compatibilidadeTipos(tipo,token);
                }
                if(!resultado){
                    System.out.println("Erro Semântico: "+ " Linha" + this.token.getLinha() + " o token " + this.token.getLexema() + " é de um tipo compatível");
                }
            }
            proximo_token();
        }else if(token.getLexema().equals("(")){
            proximo_token();
            procedimentoExpression(tipo);
            if(token.getLexema().equals(")")){
                proximo_token();
            }
        }
        else{
            this.configurarErro(token,"(,true,false,NRO,CAD,IDE");
            proximo_token();
        }
    }

    public void procedimentoVariableInit(){
        IIdentificador aux = filtrarVariaveis(this.variavelReceptor,"variavel");
        String tipo = "erro";
        if(aux == null){
            System.out.println("Erro Semântico: "+ " Linha" + this.variavelReceptor.getLinha() + " a variavel " + this.variavelReceptor.getLexema() + " não foi declarada");
        }else{
            tipo = ((IVariaveis)aux).getTipoVariavel();
        }
        if(tipo.equals("boolean")){
            if(!controleExpressãoBooleana){
                System.out.println("Erro Semântico: "+ " Linha" + this.variavelReceptor.getLinha() + " a expressão deve resultar em um tipo booleano");
            }
        }
        procedimentoExpression(tipo);
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
        this.escopo++;
        this.escopoGlobal = false;
        Token identificadorAux;
        if(token.getTipo().equals("IDE")){
            identificadorAux = token;
            proximo_token();
            if(token.getLexema().equals("(")){
                proximo_token();
                procedimentoParams();
                /**Adiciona o identificador na tabela de simbolos**/
                if(!verificarDuplicidade(identificadorAux,"procedimento",this.quantidadeParametrosAux,this.tiposParametros,this.tipoRetorno)) {
                    ProcedimentoImpl procedimento = new ProcedimentoImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,-1);
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
        this.escopoGlobal = true;
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
        this.escopo++;
        this.escopoGlobal = false;
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
                        FuncaoImpl funcao = new FuncaoImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,-1);
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
        this.escopoGlobal = true;
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
                    this.variavelReceptorTipo = "função";
                    procedimentoCallFunc("função");
                }else{
                    proximo_token();
                }
            }else if(token.getTipo().equals("CAD")){
                if(!compatibilidadeTipos(this.tipoRetorno,token)){
                    System.out.println("Erro Semântico: " + "Linha: " + token.getLinha() +
                            " O tipo de retorno da função não é compatível");
                }
                proximo_token();
            }else if(token.getTipo().equals("NRO")){
                if(!compatibilidadeTipos(this.tipoRetorno,token)){
                    System.out.println("Erro Semântico: " + "Linha: " + token.getLinha() +
                            " O tipo de retorno da função não é compatível");
                }
                proximo_token();
            }else if(token.getLexema().equals("true") || token.getLexema().equals("false")){
                if(!compatibilidadeTipos(this.tipoRetorno,token)){
                    System.out.println("Erro Semântico: " + "Linha: " + token.getLinha() +
                            " O tipo de retorno da função não é compatível");
                }
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
                    IIdentificador temp = verificarArgumentos(identificadorAux,this.argumentosAux,tipo);
                    if(tipo.equals("função")){
                        IFuncao temp2 = (IFuncao)temp;
                        if(this.variavelReceptorTipo.equals("função")){
                            if(!temp2.getTipoRetorno().equals(this.tipoRetorno)){
                                System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() +
                                        " O tipo de retorno da função não é compatível");
                            }
                        }else if(this.variavelReceptorTipo.equals("variavel")){
                            System.out.println(this.variavelReceptor.getLexema());
                            IIdentificador temp3 = filtrarVariaveis(this.variavelReceptor,"variavel");
                            if(temp3 != null) {
                                IVariaveis temp4 = (IVariaveis) temp3;
                                if (!temp2.getTipoRetorno().equals(temp4.getTipoVariavel())) {
                                    System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() +
                                            " O tipo de retorno da função não é compatível");
                                }
                            }else{
                                System.out.println("Erro Semântico: " + "Linha: " + this.variavelReceptor.getLinha() +
                                        " A variável não foi declarada");
                            }
                        }

                    }
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

    public void procedimentoTypedVariable(){
        if(primeiroType(token)){
            proximo_token(); // pulando o tipo
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
//            procedimentoVariableDeclarator();
//            if(token.getLexema().equals(",")){
//                proximo_token();
//                procedimentoVariables();
//            }
            Token identificadorAux = token;
            procedimentoVariableDeclarator();
            proximo_token();
            if(compatibilidadeTipos(this.tipoConstante, token)){ // Verifica se isso ta certo Moisas
                int controle = verificarVar(identificadorAux);
                if(controle == 0){
                    VariaveisImpl variavel = new VariaveisImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,-1);
                    variavel.setTipoVariavel(this.tipoConstante);
                    tabelaDeSimbolos.adicionarSimbolo(this.tabelaDeSimbolos.numeroSimbolos() + 1, variavel);
                }else if(controle == -1){
                    System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Já existe uma variável com o nome " + identificadorAux.getLexema());
                }else{
                    System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Já existe uma constante com o nome " + identificadorAux.getLexema());
                }
            }else{
                System.out.println("Erro Semântico: " + "Linha: " + identificadorAux.getLinha() + " Valor atribuido a variavel " + identificadorAux.getLexema() +
                        " não é um compativel com o tipo declarado na variavel");
            }
            proximo_token();
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
            Token identificadorAux = token;
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
                        this.variavelReceptor = identificadorAux;
                        this.variavelReceptorTipo = "variavel";
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
                            ConstanteImpl constante = new ConstanteImpl(this.tabelaDeSimbolos.numeroSimbolos()+1,identificadorAux,-1);
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
            Token identificadorAux = token;
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
                            this.variavelReceptor = identificadorAux;
                            this.variavelReceptorTipo = "variavel";
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
                        verificarAtribuicao(identificadorAux,token,"0","0");
                        this.variavelReceptor = identificadorAux;
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
                            this.variavelReceptor = identificadorAux;
                            this.variavelReceptorTipo = "variavel";
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
                            this.variavelReceptor = identificadorAux;
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
                    Token identificadorAux = token;
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
                                this.variavelReceptor = identificadorAux;
                                this.variavelReceptorTipo = "variavel";
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
                System.out.println("Erro Semântico: "+ "Linha: " + token.getLinha() + "variável utilizada não é: " + modeloVariavel);
            }
        }
    }

    /*public IFuncao filtrarFuncoes(Token token){
        ArrayList<IIdentificador> aux = this.tabelaDeSimbolos.getSimbolos(token,"função");
        if(aux.size() > 1){
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
            IFuncao auxiliar = (IFuncao)aux.get(0);
            return auxiliar;
        }
    }*/
    /**
     * Verifica se já existe uma função ou parametro com  a mesma assinatura.
     * */
    public boolean verificarDuplicidade(Token token,String tipo, int quantidadeParametros, ArrayList<String> tipos, String retorno){
        int controle = 0;
        boolean duplicidade = false;

        if(tipo.equals("procedimento")){
            //IIdentificador aux = this.tabelaDeSimbolos.getSimboloL(token,"procedimento");
            ArrayList<IIdentificador> aux = this.tabelaDeSimbolos.getSimbolos(token,"procedimento");
            if(aux != null) {
                for (IIdentificador aux1:aux) {
                    //IProcedimento auxProc = (IProcedimento) aux;
                    IProcedimento auxProc = (IProcedimento) aux1;
                    if (auxProc.getQuantidadeParametros() == quantidadeParametros) {
                        for (int i = 0; i < quantidadeParametros; i++) {
                            if (!auxProc.getTiposParametros().get(i).equals(tipos.get(i))) {
                                controle = 1;
                                break;
                            }
                        }
                        if (controle == 1) {
                            duplicidade = false;
                            //return false;
                        } else {
                            duplicidade = true;
                            break;
                            //return true;
                        }
                    } else {
                        duplicidade = false;
                        //return false;
                    }
                }
            }else{
                duplicidade = false;
                //return false;
            }
            return duplicidade;
        }else{
            ArrayList<IIdentificador> aux = this.tabelaDeSimbolos.getSimbolos(token,"função");
            if(aux != null){
                for(IIdentificador aux1:aux){
                    IFuncao auxFun = (IFuncao) aux1;
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
                                duplicidade = false;
                                //return false;
                            } else {
                                duplicidade = true;
                                break;
                                //return true;
                            }
                        }else{
                            duplicidade = false;
                            //return false;
                        }
                    }else {
                        duplicidade = false;
                        //return false;
                    }
                }
            }else{
                duplicidade = false;
               // return false;
            }
            return duplicidade;
        }
    }

    /**
     * Verifica se os argumentos então corretos na chamada de uma função.
     * */
    public IIdentificador verificarArgumentos(Token identificador, ArrayList<Token> argumentos, String tipo){

        int controle = 0;
        int controleTam = 0;
        int controleAux = 0;
        ArrayList<String> auxiliar = new ArrayList<>();
        ArrayList<IIdentificador> aux = this.tabelaDeSimbolos.getSimbolos(identificador,"procedimento");
        ArrayList<IIdentificador> aux2 = this.tabelaDeSimbolos.getSimbolos(identificador,"função");
        IIdentificador procFun = null;
        IIdentificador aux3;

        //Verificando os tipos dos argumentos
        for(Token token: argumentos){
            if(token.getTipo().equals("IDE")){
                //aux3 = this.tabelaDeSimbolos.getSimboloL(token,"variavel");
                /**Verificar se é uma constante*/
                aux3 = filtrarVariaveis(token,"variavel");
                if(aux3 != null){
                    if(((IVariaveis)aux3).getInicializado()){
                        auxiliar.add(((IVariaveis)aux3).getTipoVariavel());
                    }else{
                        controleAux = 1;
                        System.out.println("Erro Semântico: "+ " Linha" + token.getLinha() + " a variavel " + token.getLexema() + " não foi inicializada");
                    }
                }else{
                    aux3 = this.tabelaDeSimbolos.getSimboloL(token,"constante");
                    if(aux3 != null) {
                        auxiliar.add(((IConstante) aux3).getTipoConstante());
                    }else{
                        controleAux = 1;
                        System.out.println("Erro Semântico: "+ " Linha" + token.getLinha() + " a variavel/constante " + token.getLexema() + " não foi declarada");
                    }

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
                    controle = 0;
                    controleTam = 0;
                    IProcedimento auxProc = (IProcedimento)auxI;
                    if(auxProc.getQuantidadeParametros() == argumentos.size()){
                        controle = 0;
                        for (int i = 0; i < argumentos.size(); i++) {
                            //System.out.println("--"+auxiliar.get(i));
                            //System.out.println("-"+auxProc.getTiposParametros().get(i));
                            if (auxProc.getTiposParametros().get(i).equals(auxiliar.get(i))) {
                                controle = 1;
                                //break;
                            }else{
                                controle = 2;
                                break;
                            }
                        }
                        controleTam = 1;
                    }
                    if(controle == 1){
                        procFun = auxProc;
                        break;
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
                                //break;
                            }else{
                                controle = 2;
                                break;
                            }
                        }
                        controleTam = 1;
                    }
                    if(controle == 1){
                        System.out.println("bb");
                        procFun = auxProc;
                        break;
                    }
                }
            }

            if(controleTam == 0){
                System.out.println("Erro Semântico: "+ "Linha: " + identificador.getLinha() + " Número de argumentos incorreto");
                return null;
            }else if(controle == 2){
                System.out.println("Erro Semântico: "+ "Linha: " + identificador.getLinha() + " Tipo de argumento incorreto: algum dos argumentos da chamada de função está com o tipo incorreto");
                return null;
            }else{
                return procFun;
            }
        }else{
            return null;
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

    public int verificarVar(Token identificador){
        IIdentificador aux = null;
        int controle = -1;
        aux = this.tabelaDeSimbolos.getSimboloL(identificador,"variavel");
        if(aux == null){
            controle = 0;
            aux = this.tabelaDeSimbolos.getSimboloL(identificador,"constante");
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
        }else if(tipo.equals(atribuicao.getLexema())){/*Para variaveis com tipos compostos(Struct)*/
            return true;
        }else{
            return false;
        }
    }

    public void verificarAtribuicao(Token identificador, Token identificadorAtribuido, String valorAtribuido1, String valorAtribuido2){

        /**Lembrar dos tipos compostos*/

        IVariaveis variavelAux = filtrarVariaveis(identificador,"variavel");
        IVariaveis variavelAux2;
        IConstante constanteAux;
        Token valorDireito = null;
        String valorEsquerdo = "";
        boolean resultado = false;
        int indice = -1;
        int tipo = -1;

        if(variavelAux == null){
            System.out.println("Erro Semântico: " + "Linha: " + identificador.getLinha() + " A variável " + identificador.getLexema() +" não foi declarada");
        }else {
            //Configurando o valor da variavel do lado esquerdo;
            if(variavelAux.getModeloVariavel().equals("struct")){
                if(!valorAtribuido1.equals("0")){
                    for(int i = 0; i < variavelAux.getAtributosStruct().size();i++){
                        if(variavelAux.getAtributosStruct().get(i).equals(valorAtribuido1)){
                            indice = i;
                        }
                    }
                    valorEsquerdo = variavelAux.getTiposAtributosStruct().get(indice);
                }
            }else{
                valorEsquerdo = variavelAux.getTipoVariavel();
            }
        }

        //Configurando o valor da variavel do lado direito;
        if (identificadorAtribuido.getTipo().equals("IDE")) {
            IIdentificador identificadorLadoD = this.tabelaDeSimbolos.getSimboloL(identificadorAtribuido,"constante");
            if(identificadorLadoD == null){// Sem constantes com esse nome.
                identificadorLadoD = filtrarVariaveis(identificadorAtribuido,"variavel");
            }

            if(identificadorLadoD == null){
                System.out.println("Erro Semântico: " + "Linha: " + identificadorAtribuido.getLinha() + " A variável/constante " + identificadorAtribuido.getLexema() +" não foi declarada");
            }else{
                if(identificadorLadoD instanceof IVariaveis){
                    variavelAux2 = (IVariaveis) identificadorLadoD;
                    if(variavelAux2.getModeloVariavel().equals("struct")){
                        if(!valorAtribuido2.equals("0")){
                            for(int i = 0; i < variavelAux2.getAtributosStruct().size();i++){
                                if(variavelAux2.getAtributosStruct().get(i).equals(valorAtribuido2)){
                                    indice = i;
                                }
                            }
                            Token tokenAux = new Token("-",variavelAux2.getTiposAtributosStruct().get(indice),false);
                            valorDireito = tokenAux;
                        }
                    }else{
                        Token tokenAux = new Token("-",variavelAux2.getTipoVariavel(),false);
                        valorDireito = tokenAux;
                    }
                }else if(identificadorLadoD instanceof IConstante){
                    constanteAux = (IConstante) identificadorLadoD;
                    Token tokenAux = new Token("-",constanteAux.getTipoConstante(),false);
                    valorDireito = tokenAux;
                }
            }
        } else {
            valorDireito = identificadorAtribuido;
        }
        //variavelAux.getTipoVariavel()
        if (!compatibilidadeTipos(valorEsquerdo, valorDireito)) {
            System.out.println("Erro Semântico: " + "Linha: " + identificador.getLinha() + " Está sendo atribuído um tipo incorreto na variavel " + identificador.getLexema());
        }
    }

    public IVariaveis filtrarVariaveis(Token identificador, String tipo){
        ArrayList<IIdentificador> variaveis = this.tabelaDeSimbolos.getSimbolos(identificador,tipo);
        int controleGlobal = 0;
        IVariaveis variavelAux = null;

        for(IIdentificador variavel: variaveis){
            variavelAux = (IVariaveis)variavel;
            if(variavelAux.getEscopo() == this.escopo){
                controleGlobal = 1;
                break;
            }
        }

        if(controleGlobal == 0){
            for(IIdentificador variavel: variaveis){
                variavelAux = (IVariaveis)variavel;
                if(variavelAux.getIdentificador().getEscopo() == -1){
                    controleGlobal = 2;
                    break;
                }
            }
        }
        if(controleGlobal == 0){
            return null;
        }else{
            return variavelAux;
        }

    }

    /*public boolean verificarDuplicidade(Token token,String tipo, int quantidadeParametros, ArrayList<String> tipos, String retorno){
        int controle = 0;

        if(tipo.equals("procedimento")){
            //IIdentificador aux = this.tabelaDeSimbolos.getSimboloL(token,"procedimento");
            ArrayList<IIdentificador> aux = this.tabelaDeSimbolos.getSimbolos(token,"procedimento");
            if(aux != null) {
                for (IIdentificador aux1:aux) {
                    //IProcedimento auxProc = (IProcedimento) aux;
                    IProcedimento auxProc = (IProcedimento) aux1;
                    if (auxProc.getQuantidadeParametros() == quantidadeParametros) {
                        for (int i = 0; i < quantidadeParametros; i++) {
                            if (!auxProc.getTiposParametros().get(i).equals(tipos.get(i))) {
                                controle = 1;
                                break;
                            }
                        }
                        if (controle == 1) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }else{
                    return false;
                }
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
    }*/
}

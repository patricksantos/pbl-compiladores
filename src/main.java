public class main {

    public static void main(String[] args) {
        String texto = "teste 1 teste 2 teste /* tesssteeeeskfadfads */ teste 1 teste 2 teste 3  teste 1 teste 2 teste 3";
        DelimitadorComentario retorno = new DelimitadorComentario();
        System.out.println(retorno.getLexema(texto, 0));
    }

}

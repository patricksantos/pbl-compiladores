public class main {

    public static void main(String[] args) {
        DelimitadorComentario retorno = new DelimitadorComentario();
        String texto = "teste 1 teste 2 /* tessste*eeesk/fadf*ads */ testeteste 1 teste 2 teste 3  teste 1 teste 2 teste 3 // oiiiii \\n afdsfsaf";
        String texto2 = "// oii*/*iii \\n teste 1 teste 2 /* tesssteeeeskfadfads */ testeteste 1 teste 2 teste 3  teste 1 teste 2 teste 3  afdsfsaf";

        System.out.println(retorno.getLexema(texto, 0));
        System.out.println(retorno.getLexema(texto2, 0));
    }

}

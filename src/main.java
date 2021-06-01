import domain.entities.Token;
import modules.AnalisadorLexico.ControllerAnalisadorLexico;
import modules.Controller;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;
import modules.TabelaSimbolos.usecases.impl.TabelaSimbolosImpl;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        /*Token a = new Token("a","b",false);
        Token b = new Token("a","b",false);
        Token c = a;
        System.out.println("oi");
        if(c == a){
            System.out.println("ok");
        }*/

        //ControllerAnalisadorLexico controllerAnalisadorLexico = new ControllerAnalisadorLexico();
        Controller controller = new Controller();
        controller.iniciarLeitura();
        //controllerAnalisadorLexico.iniciarLeitura();

    }
}

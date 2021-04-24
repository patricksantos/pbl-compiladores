import modules.AnalisadorLexico.ControllerAnalisadorLexico;
import modules.Controller;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;
import modules.TabelaSimbolos.usecases.impl.TabelaSimbolosImpl;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        //ControllerAnalisadorLexico controllerAnalisadorLexico = new ControllerAnalisadorLexico();
        Controller controller = new Controller();
        controller.iniciarLeitura();
        //controllerAnalisadorLexico.iniciarLeitura();

    }
}

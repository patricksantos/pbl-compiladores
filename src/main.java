import modules.AnalisadorLexico.ControllerAnalisadorLexico;
import modules.TabelaSimbolos.usecases.facade.ITabelaSimbolos;
import modules.TabelaSimbolos.usecases.impl.TabelaSimbolosImpl;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        ControllerAnalisadorLexico controllerAnalisadorLexico = new ControllerAnalisadorLexico();

        controllerAnalisadorLexico.iniciarLeitura();

    }
}

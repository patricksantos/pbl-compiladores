import modules.AnalisadorLexico.ControllerAnalisadorLexico;

import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {

        ControllerAnalisadorLexico controllerAnalisadorLexico = new ControllerAnalisadorLexico();

        controllerAnalisadorLexico.iniciarLeitura();
        //controllerAnalisadorLexico.escreverToken();

    }
}

package modules.AnalisadorLexico.usecases.facades.automatos;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAutomato;

import java.util.ArrayList;

public interface IDelimitadorComentario  extends IAutomato {

    Token getToken(ArrayList<String> arquivo, String texto, int linhaFinal, int posicao);

    int getLinhaFinal();
}

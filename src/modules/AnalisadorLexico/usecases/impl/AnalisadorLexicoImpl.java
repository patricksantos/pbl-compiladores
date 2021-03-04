package modules.AnalisadorLexico.usecases.impl;

import modules.AnalisadorLexico.entities.Token;
import modules.AnalisadorLexico.usecases.facades.IAnalisadorLexico;
import modules.AnalisadorLexico.usecases.facades.IAutomato;

public class AnalisadorLexicoImpl implements IAnalisadorLexico {

    IAutomato delimitadorComentario;

    public AnalisadorLexicoImpl(IAutomato delimitadorComentario) {
        this.delimitadorComentario = delimitadorComentario;
    }

    @Override
    public Token analise(String texto, int posicao) {
        return delimitadorComentario.getToken(texto, posicao);
    }

}

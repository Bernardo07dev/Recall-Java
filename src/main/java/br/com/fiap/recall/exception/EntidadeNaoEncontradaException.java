package br.com.fiap.recall.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}

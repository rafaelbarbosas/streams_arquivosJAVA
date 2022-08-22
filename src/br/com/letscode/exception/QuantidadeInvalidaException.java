package br.com.letscode.exception;

public class QuantidadeInvalidaException extends RuntimeException {
    public QuantidadeInvalidaException(String errorMessage) {
        super(errorMessage);
    }
}

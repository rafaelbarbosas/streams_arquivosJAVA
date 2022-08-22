package br.com.letscode.exception;

public class FalhaInicializandoBancoDeDadosError extends Error {
    public FalhaInicializandoBancoDeDadosError(String errorMessage) {
        super(errorMessage);
    }

    public FalhaInicializandoBancoDeDadosError(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }
}

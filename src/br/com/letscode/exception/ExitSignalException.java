package br.com.letscode.exception;

public class ExitSignalException extends Exception {
    public ExitSignalException(String errorMessage) {
        super(errorMessage);
    }
}

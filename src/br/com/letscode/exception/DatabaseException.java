package br.com.letscode.exception;

public class DatabaseException extends Exception {
    public DatabaseException(String errorMessage) {
        super(errorMessage);
    }

    public DatabaseException(String errorMessage, Exception cause) {
        super(errorMessage, cause);
    }
}

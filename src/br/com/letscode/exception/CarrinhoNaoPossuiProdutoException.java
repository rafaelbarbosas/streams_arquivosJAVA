package br.com.letscode.exception;

public class CarrinhoNaoPossuiProdutoException extends RuntimeException {
    public CarrinhoNaoPossuiProdutoException(String errorMessage) {
        super(errorMessage);
    }
}

package br.com.planeta_atlantida.rbs.api.exception;

public class AbstractException extends RuntimeException {

    private final String message;

    public AbstractException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
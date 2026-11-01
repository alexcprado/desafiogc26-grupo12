package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoriaVaziaException extends AbstractException {

    public CategoriaVaziaException(String message) {
        super(message);
    }

}

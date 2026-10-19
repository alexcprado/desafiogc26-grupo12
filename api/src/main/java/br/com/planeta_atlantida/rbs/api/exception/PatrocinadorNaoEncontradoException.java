package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatrocinadorNaoEncontradoException extends AbstractException {

    public PatrocinadorNaoEncontradoException(String message) {
        super(message);
    }

}

package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArquivoVazioException extends AbstractException {

    public ArquivoVazioException(String message) {
        super(message);
    }

}

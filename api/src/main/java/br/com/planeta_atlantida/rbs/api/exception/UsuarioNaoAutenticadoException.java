package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UsuarioNaoAutenticadoException extends AbstractException {

    public UsuarioNaoAutenticadoException(String message) {
        super(message);
    }

}

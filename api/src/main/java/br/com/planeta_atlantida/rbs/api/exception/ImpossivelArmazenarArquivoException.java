package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossivelArmazenarArquivoException extends AbstractException {

    public ImpossivelArmazenarArquivoException(String message) {
        super(message);
    }

}

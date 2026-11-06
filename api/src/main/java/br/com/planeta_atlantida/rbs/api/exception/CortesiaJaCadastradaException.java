package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CortesiaJaCadastradaException extends AbstractException {

  public CortesiaJaCadastradaException(String message) {
    super(message);
  }

}

package br.com.planeta_atlantida.rbs.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SemPermissaoParaBaixarDocumentoException extends AbstractException {

  public SemPermissaoParaBaixarDocumentoException(String message) {
    super(message);
  }

}

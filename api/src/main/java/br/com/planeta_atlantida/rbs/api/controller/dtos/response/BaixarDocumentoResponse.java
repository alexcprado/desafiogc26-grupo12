package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaixarDocumentoResponse {

    private Long idDocumento;
    private Resource arquivo;
    private String nomeArquivo;
    private String tipoMime;
    private Long tamanhoBytes;

}

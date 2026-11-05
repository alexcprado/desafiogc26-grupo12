package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import br.com.planeta_atlantida.rbs.api.enums.OrigemDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoResponse {

    private Long id;
    private String nomeArquivo;
    private CategoriaDocumento categoria;
    private String tipoMime;
    private Long tamanhoBytes;
    private OrigemDocumento origem;
    private Instant dataHoraCriacao;

}

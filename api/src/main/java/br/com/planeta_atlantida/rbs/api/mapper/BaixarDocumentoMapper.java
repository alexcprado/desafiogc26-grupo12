package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BaixarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class BaixarDocumentoMapper {

    public static BaixarDocumentoResponse toResponse(Resource arquivo, Documento entity) {
        return BaixarDocumentoResponse.builder()
                .idDocumento(entity.getId())
                .arquivo(arquivo)
                .nomeArquivo(entity.getNomeArquivo())
                .tipoMime(entity.getTipoMime())
                .tamanhoBytes(entity.getTamanhoBytes())
                .build();
    }

}

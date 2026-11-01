package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.EnviarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import br.com.planeta_atlantida.rbs.api.enums.OrigemDocumento;
import org.springframework.stereotype.Component;

@Component
public class EnviarDocumentoMapper {

    public static Documento toEntity(
            Patrocinio patrocinio,
            Usuario usuario,
            String nomeArquivo,
            CategoriaDocumento categoria,
            String chaveArmazenamento,
            String tipoMime,
            Long tamanhoBytes,
            OrigemDocumento origem) {
        return Documento.builder()
                .patrocinio(patrocinio)
                .usuarioEnvio(usuario)
                .nomeArquivo(nomeArquivo)
                .categoria(categoria)
                .chaveArmazenamento(chaveArmazenamento)
                .tipoMime(tipoMime)
                .tamanhoBytes(tamanhoBytes)
                .origem(origem)
                .build();
    }

    public static EnviarDocumentoResponse toResponse(Long id) {
        return EnviarDocumentoResponse.builder()
                .id(id)
                .build();
    }

}

package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.DetalhesPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.DocumentoResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DetalhesPatrocinioMapper {

    public static DetalhesPatrocinioResponse toResponse(Patrocinio patrocinio, List<Documento> documentos) {
        return DetalhesPatrocinioResponse.builder()
                .idPatrocinio(patrocinio.getId())
                .anoEvento(patrocinio.getEvento().getAno())
                .cota(patrocinio.getCota())
                .patrocinador(toPatrocinadorResponse(patrocinio.getPatrocinador()))
                .documentos(documentos.stream()
                        .map(DetalhesPatrocinioMapper::toDocumentoResponse)
                        .toList())
                .build();
    }

    private static PatrocinadorResponse toPatrocinadorResponse(Patrocinador patrocinador) {
        return PatrocinadorResponse.builder()
                .id(patrocinador.getId())
                .cnpj(patrocinador.getCnpj())
                .nome(patrocinador.getNome())
                .segmento(patrocinador.getSegmento())
                .contatoPrincipal(patrocinador.getContatoPrincipal())
                .telefone(patrocinador.getTelefone())
                .status(patrocinador.getStatus())
                .build();
    }

    private static DocumentoResponse toDocumentoResponse(Documento documento) {
        return DocumentoResponse.builder()
                .id(documento.getId())
                .nomeArquivo(documento.getNomeArquivo())
                .categoria(documento.getCategoria())
                .tipoMime(documento.getTipoMime())
                .tamanhoBytes(documento.getTamanhoBytes())
                .origem(documento.getOrigem())
                .dataHoraCriacao(documento.getDataHoraCriacao())
                .build();
    }

}

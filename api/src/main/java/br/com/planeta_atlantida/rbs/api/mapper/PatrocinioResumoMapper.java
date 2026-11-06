package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinioResumoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import org.springframework.stereotype.Component;

@Component
public class PatrocinioResumoMapper {

    public static PatrocinioResumoResponse toResponse(Patrocinio patrocinio) {
        return PatrocinioResumoResponse.builder()
                .id(patrocinio.getId())
                .anoEvento(patrocinio.getEvento().getAno())
                .cota(patrocinio.getCota())
                .patrocinador(toPatrocinadorResponse(patrocinio.getPatrocinador()))
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

}

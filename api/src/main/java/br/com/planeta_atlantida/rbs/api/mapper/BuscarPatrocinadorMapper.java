package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import org.springframework.stereotype.Component;

@Component
public class BuscarPatrocinadorMapper {

    public static PatrocinadorResponse toResponse(Patrocinador patrocinador) {
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

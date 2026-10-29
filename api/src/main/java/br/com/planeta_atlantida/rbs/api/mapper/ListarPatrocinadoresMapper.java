package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPatrocinadoresResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import org.springframework.stereotype.Component;

@Component
public class ListarPatrocinadoresMapper {

    public static ListarPatrocinadoresResponse toResponse(Patrocinador entity) {
        return ListarPatrocinadoresResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }

}

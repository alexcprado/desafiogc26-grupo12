package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarCortesiasResponse;
import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import org.springframework.stereotype.Component;

@Component
public class ListarCortesiaMapper {

    public static ListarCortesiasResponse toResponse(Cortesia entity) {
        return ListarCortesiasResponse.builder()
                .id(entity.getId())
                .idPatrocinio(entity.getPatrocinio().getId())
                .setor(entity.getSetor())
                .tipo(entity.getTipo())
                .quantidade(entity.getQuantidade())
                .build();
    }

}

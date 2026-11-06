package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarCortesiaRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarCortesiaResponse;
import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import org.springframework.stereotype.Component;

@Component
public class AdicionarCortesiaMapper {

    public static Cortesia toEntity(Patrocinio patrocinio, AdicionarCortesiaRequest request) {
        return Cortesia.builder()
                .patrocinio(patrocinio)
                .setor(request.getSetor())
                .tipo(request.getTipo())
                .quantidade(request.getQuantidade())
                .build();
    }

    public static AdicionarCortesiaResponse toResponse(Cortesia entity) {
        return AdicionarCortesiaResponse.builder()
                .idCortesia(entity.getId())
                .build();
    }

}

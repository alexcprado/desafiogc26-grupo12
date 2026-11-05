package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AlterarPatrocinadorResponse;
import org.springframework.stereotype.Component;

@Component
public class AlterarPatrocinadorMapper {

    public static AlterarPatrocinadorResponse toResponse(Long id) {
        return AlterarPatrocinadorResponse.builder()
                .idPatrocinador(id)
                .build();
    }

}

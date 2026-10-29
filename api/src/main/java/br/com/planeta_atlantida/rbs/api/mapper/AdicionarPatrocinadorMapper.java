package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import org.springframework.stereotype.Component;

@Component
public class AdicionarPatrocinadorMapper {

    public static Patrocinador toEntity(AdicionarPatrocinadorRequest request) {
        return Patrocinador.builder()
                .nome(request.getNome())
                .cnpj(request.getCnpj())
                .build();
    }

    public static AdicionarPatrocinadorResponse toResponse(Long idPatrocinador) {
        return AdicionarPatrocinadorResponse.builder()
                .idPatrocinador(idPatrocinador)
                .build();
    }

}

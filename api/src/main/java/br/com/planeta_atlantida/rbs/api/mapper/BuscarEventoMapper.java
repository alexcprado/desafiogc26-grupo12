package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BuscarEventoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Evento;
import org.springframework.stereotype.Component;

@Component
public class BuscarEventoMapper {

    public static BuscarEventoResponse toResponse(Evento evento) {
        return BuscarEventoResponse.builder()
                .id(evento.getId())
                .ano(evento.getAno())
                .build();
    }

}

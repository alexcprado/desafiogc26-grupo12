package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.domain.Evento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.enums.CotaPatrocinio;
import org.springframework.stereotype.Component;

@Component
public class AdicionarPatrocinioMapper {

    public static Patrocinio toEntity(Evento evento, Patrocinador patrocinador, CotaPatrocinio cota) {
        return Patrocinio.builder()
                .evento(evento)
                .patrocinador(patrocinador)
                .cota(cota).build();
    }

}

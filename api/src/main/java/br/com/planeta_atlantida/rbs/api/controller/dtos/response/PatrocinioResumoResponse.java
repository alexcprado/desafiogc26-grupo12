package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import br.com.planeta_atlantida.rbs.api.enums.CotaPatrocinio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrocinioResumoResponse {

    private Long id;
    private Short anoEvento;
    private CotaPatrocinio cota;
    private PatrocinadorResponse patrocinador;

}

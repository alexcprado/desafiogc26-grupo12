package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarPatrocinioResponse {

    private Long idPatrocinio;
    private Long idPatrocinador;
    private Long idEvento;

}

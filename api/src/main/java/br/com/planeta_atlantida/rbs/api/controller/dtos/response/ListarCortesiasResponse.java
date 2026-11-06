package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import br.com.planeta_atlantida.rbs.api.enums.SetorCortesia;
import br.com.planeta_atlantida.rbs.api.enums.TipoCortesia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarCortesiasResponse {

    private Long id;
    private Long idPatrocinio;
    private SetorCortesia setor;
    private TipoCortesia tipo;
    private Integer quantidade;


}

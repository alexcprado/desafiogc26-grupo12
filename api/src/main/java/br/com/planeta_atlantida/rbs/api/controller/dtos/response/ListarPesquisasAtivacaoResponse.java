package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarPesquisasAtivacaoResponse {

    private Long id;
    private Long idPatrocinio;
    private String pergunta1;
    private String pergunta2;
    private String pergunta3;

}

package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import br.com.planeta_atlantida.rbs.api.enums.StatusPatrocinador;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatrocinadorResponse {

    private Long id;
    private String cnpj;
    private String nome;
    private String segmento;
    private String contatoPrincipal;
    private String telefone;
    private StatusPatrocinador status;

}

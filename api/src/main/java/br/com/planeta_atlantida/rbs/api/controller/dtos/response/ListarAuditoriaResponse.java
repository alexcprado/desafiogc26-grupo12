package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListarAuditoriaResponse {

    private Long id;
    private String emailUsuario;
    private AcaoAuditoria acao;
    private EntidadeAuditoria entidade;
    private String nomeRecurso;
    private Instant dataHora;

}

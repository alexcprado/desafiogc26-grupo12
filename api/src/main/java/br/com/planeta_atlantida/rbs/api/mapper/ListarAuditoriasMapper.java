package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarAuditoriaResponse;
import br.com.planeta_atlantida.rbs.api.domain.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class ListarAuditoriasMapper {

    public static ListarAuditoriaResponse toResponse(Auditoria entity) {
        return ListarAuditoriaResponse.builder()
                .id(entity.getId())
                .emailUsuario(entity.getEmailUsuario())
                .acao(entity.getAcao())
                .entidade(entity.getEntidade())
                .nomeRecurso(entity.getNomeRecurso())
                .dataHora(entity.getDataHora())
                .build();
    }

}

package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.domain.Auditoria;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import org.springframework.stereotype.Component;

@Component
public class RegistrarAuditoriaMapper {

    public static Auditoria toEntity(
            Usuario usuario,
            AcaoAuditoria acao,
            EntidadeAuditoria entidade,
            Long idEntidade,
            String nomeRecurso
    ) {
        return Auditoria.builder()
                .usuario(usuario)
                .nomeUsuario(usuario.getNome())
                .emailUsuario(usuario.getEmail())
                .acao(acao)
                .entidade(entidade)
                .idEntidade(idEntidade)
                .nomeRecurso(nomeRecurso)
                .build();
    }

}

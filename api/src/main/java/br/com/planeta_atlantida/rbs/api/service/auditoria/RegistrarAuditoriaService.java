package br.com.planeta_atlantida.rbs.api.service.auditoria;

import br.com.planeta_atlantida.rbs.api.domain.Auditoria;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.mapper.RegistrarAuditoriaMapper;
import br.com.planeta_atlantida.rbs.api.repository.AuditoriaRepository;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrarAuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    @Transactional
    public void registrar(AcaoAuditoria acao, EntidadeAuditoria entidade, Long idEntidade, String nomeRecurso) {
        Usuario usuario = buscarUsuarioLogadoService.buscar();

        salvar(acao, entidade, idEntidade, usuario, nomeRecurso);

    }

    @Transactional
    public void registrarLogin(Usuario usuario) {
        salvar(AcaoAuditoria.REALIZOU_LOGIN, EntidadeAuditoria.AUTENTICACAO, null, usuario, null);
    }

    private void salvar(
            AcaoAuditoria acao,
            EntidadeAuditoria entidade,
            Long idEntidade,
            Usuario usuario,
            String nomeRecurso
    ) {

        Auditoria auditoria = RegistrarAuditoriaMapper.toEntity(usuario, acao, entidade, idEntidade, nomeRecurso);

        auditoriaRepository.save(auditoria);

    }

}

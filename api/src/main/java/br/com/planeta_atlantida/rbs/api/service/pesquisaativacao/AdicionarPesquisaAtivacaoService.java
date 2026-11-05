package br.com.planeta_atlantida.rbs.api.service.pesquisaativacao;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPesquisaAtivacaoRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPesquisaAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.PesquisaAtivacao;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.exception.SemPermissaoParaEnviarPesquisaAtivacaoException;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPesquisaAtivacaoMapper;
import br.com.planeta_atlantida.rbs.api.repository.PesquisaAtivacaoRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import br.com.planeta_atlantida.rbs.api.validator.PesquisaAtivacaoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdicionarPesquisaAtivacaoService {

    private final PesquisaAtivacaoRepository pesquisaAtivacaoRepository;
    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final PatrocinioValidator patrocinioValidator;
    private final PesquisaAtivacaoValidator pesquisaAtivacaoValidator;

    @PreAuthorize("hasAnyRole('ADMIN', 'PATROCINADOR')")
    @Transactional
    public AdicionarPesquisaAtivacaoResponse adicionar(Long idPatrocinio, AdicionarPesquisaAtivacaoRequest request) {

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);

        patrocinioValidator.validarPermissao(patrocinio);
        pesquisaAtivacaoValidator.validarCotaMaster(patrocinio);
        pesquisaAtivacaoValidator.validarPesquisaAtivacaoJaExiste(idPatrocinio);

        PesquisaAtivacao novaPesquisa = AdicionarPesquisaAtivacaoMapper.toEntity(request, patrocinio);

        pesquisaAtivacaoRepository.save(novaPesquisa);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.ADICIONOU_PESQUISA_ATIVACAO,
                EntidadeAuditoria.PESQUISA_ATIVACAO,
                novaPesquisa.getId(),
                "Pesquisa de ativaçao - " + patrocinio.getPatrocinador().getNome()
        );

        return AdicionarPesquisaAtivacaoMapper.toResponse(novaPesquisa.getId());

    }

}

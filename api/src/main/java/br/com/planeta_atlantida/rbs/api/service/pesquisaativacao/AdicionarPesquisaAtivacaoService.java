package br.com.planeta_atlantida.rbs.api.service.pesquisaativacao;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPesquisaAtivacaoRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPesquisaAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.PesquisaAtivacao;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.CotaPatrocinio;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.exception.CotaNaoPermitidaException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinioNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.exception.PesquisaAtivacaoJaExisteException;
import br.com.planeta_atlantida.rbs.api.exception.SemPermissaoParaEnviarPesquisaAtivacaoException;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPesquisaAtivacaoMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import br.com.planeta_atlantida.rbs.api.repository.PesquisaAtivacaoRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdicionarPesquisaAtivacaoService {

    private final PesquisaAtivacaoRepository pesquisaAtivacaoRepository;
    private final PatrocinioRepository patrocinioRepository;
    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PATROCINADOR')")
    @Transactional
    public AdicionarPesquisaAtivacaoResponse adicionar(Long idPatrocinio, AdicionarPesquisaAtivacaoRequest request) {

        Patrocinio patrocinio = patrocinioRepository.findById(idPatrocinio)
                .orElseThrow(() ->
                        new PatrocinioNaoEncontradoException(Mensagens.PATROCINIO_NAO_ENCONTRADO
                        ));

        validarPermissao(patrocinio);
        validarCotaMaster(patrocinio);

        if (pesquisaAtivacaoRepository.existsByPatrocinio_Id(idPatrocinio)) {
            throw new PesquisaAtivacaoJaExisteException(Mensagens.PESQUISA_ATIVACAO_JA_EXISTE);
        }

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

    private void validarCotaMaster(Patrocinio patrocinio) {
        if (patrocinio.getCota() != CotaPatrocinio.MASTER) {
            throw new CotaNaoPermitidaException(Mensagens.COTA_NAO_PERMITE_PESQUISA_ATIVACAO);
        }
    }

    private void validarPermissao(Patrocinio patrocinio) {
        Usuario usuario = buscarUsuarioLogadoService.buscar();

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            return;
        }

        if (usuario.getTipo() != TipoUsuario.PATROCINADOR
            || usuario.getPatrocinador() == null
            || !Objects.equals(
                    usuario.getPatrocinador().getId(),
                    patrocinio.getPatrocinador().getId()
            )) {
                throw new SemPermissaoParaEnviarPesquisaAtivacaoException(Mensagens.SEM_PERMISSAO_PARA_ENVIAR_PESQUISA_ATIVACAO);
        }
    }

}

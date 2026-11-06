package br.com.planeta_atlantida.rbs.api.service.pesquisaativacao;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPesquisasAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.PesquisaAtivacao;
import br.com.planeta_atlantida.rbs.api.exception.PesquisaAtivacaoNaoEncontradaException;
import br.com.planeta_atlantida.rbs.api.mapper.ListarPesquisaAtivacaoMapper;
import br.com.planeta_atlantida.rbs.api.repository.PesquisaAtivacaoRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscarPesquisaAtivacaoService {

    private final PesquisaAtivacaoRepository pesquisaAtivacaoRepository;
    private final PatrocinioValidator patrocinioValidator;

    @PreAuthorize("hasAnyRole('ADMIN', 'PATROCINADOR')")
    @Transactional(readOnly = true)
    public ListarPesquisasAtivacaoResponse buscar(Long idPatrocinio) {

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);

        patrocinioValidator.validarPermissao(patrocinio);

        PesquisaAtivacao pesquisa = pesquisaAtivacaoRepository.findByPatrocinio_Id(idPatrocinio)
                .orElseThrow(() -> new PesquisaAtivacaoNaoEncontradaException(
                        Mensagens.PESQUISA_ATIVACAO_NAO_ENCONTRADA
                ));

        return ListarPesquisaAtivacaoMapper.toResponse(pesquisa);

    }

}

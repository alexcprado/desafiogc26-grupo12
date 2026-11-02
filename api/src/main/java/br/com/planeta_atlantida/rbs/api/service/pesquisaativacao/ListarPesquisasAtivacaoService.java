package br.com.planeta_atlantida.rbs.api.service.pesquisaativacao;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPesquisasAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.mapper.ListarPesquisaAtivacaoMapper;
import br.com.planeta_atlantida.rbs.api.repository.PesquisaAtivacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarPesquisasAtivacaoService {

    private final PesquisaAtivacaoRepository pesquisaAtivacaoRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<ListarPesquisasAtivacaoResponse> listar(Pageable pageable) {
        return pesquisaAtivacaoRepository.findAll(pageable)
                .map(ListarPesquisaAtivacaoMapper::toResponse);
    }

}

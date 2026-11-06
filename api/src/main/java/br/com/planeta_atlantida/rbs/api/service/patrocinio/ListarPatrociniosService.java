package br.com.planeta_atlantida.rbs.api.service.patrocinio;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinioResumoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.mapper.PatrocinioResumoMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarPatrociniosService {

    private final PatrocinioRepository patrocinioRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<PatrocinioResumoResponse> listar(Long idEvento, Pageable pageable) {

        Page<Patrocinio> patrocinios = idEvento == null
                ? patrocinioRepository.findAll(pageable)
                : patrocinioRepository.findByEvento_Id(idEvento, pageable);

        return patrocinios.map(PatrocinioResumoMapper::toResponse);

    }

}

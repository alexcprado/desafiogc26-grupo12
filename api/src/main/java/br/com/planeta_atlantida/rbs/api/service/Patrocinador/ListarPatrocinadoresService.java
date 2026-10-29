package br.com.planeta_atlantida.rbs.api.service.Patrocinador;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPatrocinadoresResponse;
import br.com.planeta_atlantida.rbs.api.enums.StatusPatrocinador;
import br.com.planeta_atlantida.rbs.api.mapper.ListarPatrocinadoresMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarPatrocinadoresService {

    private final PatrocinadorRepository patrocinadorRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<ListarPatrocinadoresResponse> listar(Pageable pageable) {
        return patrocinadorRepository.findByStatus(StatusPatrocinador.ATIVO, pageable)
                .map(ListarPatrocinadoresMapper::toResponse);
    }

}

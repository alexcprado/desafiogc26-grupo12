package br.com.planeta_atlantida.rbs.api.service.auditoria;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarAuditoriaResponse;
import br.com.planeta_atlantida.rbs.api.mapper.ListarAuditoriasMapper;
import br.com.planeta_atlantida.rbs.api.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarAuditoriasService {

    private final AuditoriaRepository auditoriaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<ListarAuditoriaResponse> listar(Pageable pageable) {
        return auditoriaRepository.findAll(pageable)
                .map(ListarAuditoriasMapper::toResponse);
    }

}

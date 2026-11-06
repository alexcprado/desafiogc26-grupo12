package br.com.planeta_atlantida.rbs.api.service.evento;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BuscarEventoResponse;
import br.com.planeta_atlantida.rbs.api.mapper.BuscarEventoMapper;
import br.com.planeta_atlantida.rbs.api.repository.EventoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListarEventosService {

    private final EventoRepository eventoRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<BuscarEventoResponse> listar(Pageable pageable) {
        return eventoRepository.findAll(pageable)
                .map(BuscarEventoMapper::toResponse);
    }

}

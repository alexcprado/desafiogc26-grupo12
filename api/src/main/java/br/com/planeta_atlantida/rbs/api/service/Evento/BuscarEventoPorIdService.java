package br.com.planeta_atlantida.rbs.api.service.Evento;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BuscarEventoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Evento;
import br.com.planeta_atlantida.rbs.api.exception.EventoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.mapper.BuscarEventoMapper;
import br.com.planeta_atlantida.rbs.api.repository.EventoRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscarEventoPorIdService {

    private final EventoRepository eventoRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public BuscarEventoResponse buscar(Long id) {

        Evento evento = eventoRepository
                .findById(id)
                .orElseThrow(() ->
                        new EventoNaoEncontradoException(Mensagens.EVENTO_NAO_ENCONTRADO
                        ));

        return BuscarEventoMapper.toResponse(evento);

    }

}

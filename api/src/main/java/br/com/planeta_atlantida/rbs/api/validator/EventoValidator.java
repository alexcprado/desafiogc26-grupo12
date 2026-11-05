package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.domain.Evento;
import br.com.planeta_atlantida.rbs.api.exception.EventoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.repository.EventoRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventoValidator {

    private final EventoRepository eventoRepository;

    public Evento validarEventoExiste(Long idEvento) {
        return eventoRepository.findById(idEvento)
                .orElseThrow(() ->
                        new EventoNaoEncontradoException(Mensagens.EVENTO_NAO_ENCONTRADO)
                );
    }

}

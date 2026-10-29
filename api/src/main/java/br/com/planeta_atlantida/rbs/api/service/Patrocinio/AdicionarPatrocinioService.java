package br.com.planeta_atlantida.rbs.api.service.Patrocinio;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinioRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Evento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.exception.EventoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorJaVinculadoException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPatrocinioMapper;
import br.com.planeta_atlantida.rbs.api.repository.EventoRepository;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarPatrocinioService {

    private final EventoRepository eventoRepository;
    private final PatrocinadorRepository patrocinadorRepository;
    private final PatrocinioRepository patrocinioRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarPatrocinioResponse adicionar(Long idEvento, AdicionarPatrocinioRequest request) {

        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() ->
                new EventoNaoEncontradoException(Mensagens.EVENTO_NAO_ENCONTRADO)
                );

        Patrocinador patrocinador = patrocinadorRepository.findById(request.getIdPatrocinador())
                .orElseThrow(() ->
                        new PatrocinadorNaoEncontradoException(Mensagens.PATROCINADOR_NAO_ENCONTRADO)
                );

        if (patrocinioRepository.existsByEvento_IdAndPatrocinador_Id(idEvento, request.getIdPatrocinador())) {
            throw new PatrocinadorJaVinculadoException(Mensagens.PATROCINADOR_JA_VINCULADO);
        }

        Patrocinio novoPatrocinio = AdicionarPatrocinioMapper.toEntity(evento, patrocinador, request.getCota());

        patrocinioRepository.save(novoPatrocinio);

        return AdicionarPatrocinioMapper.toResponse(novoPatrocinio.getId(), patrocinador.getId(), evento.getId());

    }

}

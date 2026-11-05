package br.com.planeta_atlantida.rbs.api.service.patrocinio;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinioRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Evento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.exception.EventoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorJaVinculadoException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPatrocinioMapper;
import br.com.planeta_atlantida.rbs.api.repository.EventoRepository;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.EventoValidator;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinadorValidator;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarPatrocinioService {

    private final PatrocinioRepository patrocinioRepository;
    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final EventoValidator eventoValidator;
    private final PatrocinadorValidator patrocinadorValidator;
    private final PatrocinioValidator patrocinioValidator;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarPatrocinioResponse adicionar(Long idEvento, AdicionarPatrocinioRequest request) {

        Evento evento = eventoValidator.validarEventoExiste(idEvento);

        Patrocinador patrocinador = patrocinadorValidator.validarPatrocinadorExiste(request.getIdPatrocinador());

        patrocinioValidator.validarPatrocinadorJaVinculado(idEvento, patrocinador.getId());

        Patrocinio novoPatrocinio = AdicionarPatrocinioMapper.toEntity(evento, patrocinador, request.getCota());

        patrocinioRepository.save(novoPatrocinio);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.ADICIONOU_PATROCINIO,
                EntidadeAuditoria.PATROCINIO,
                novoPatrocinio.getId(),
                null
        );

        return AdicionarPatrocinioMapper.toResponse(novoPatrocinio.getId(), patrocinador.getId(), evento.getId());

    }

}

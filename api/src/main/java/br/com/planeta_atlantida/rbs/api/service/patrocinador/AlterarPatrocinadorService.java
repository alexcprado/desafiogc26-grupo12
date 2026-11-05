package br.com.planeta_atlantida.rbs.api.service.patrocinador;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AlterarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AlterarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.mapper.AlterarPatrocinadorMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinadorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlterarPatrocinadorService {

    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final PatrocinadorValidator patrocinadorValidator;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AlterarPatrocinadorResponse alterar(Long id, AlterarPatrocinadorRequest request) {

        Patrocinador patrocinador = patrocinadorValidator.validarPatrocinadorExiste(id);

        patrocinador.setNome(request.getNome());
        patrocinador.setSegmento(request.getSegmento());
        patrocinador.setContatoPrincipal(request.getContatoPrincipal());
        patrocinador.setTelefone(request.getTelefone());

        registrarAuditoriaService.registrar(
                AcaoAuditoria.ALTEROU_PATROCINADOR,
                EntidadeAuditoria.PATROCINADOR,
                patrocinador.getId(),
                patrocinador.getNome()
        );

        return AlterarPatrocinadorMapper.toResponse(patrocinador.getId());

    }

}

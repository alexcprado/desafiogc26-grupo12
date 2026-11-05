package br.com.planeta_atlantida.rbs.api.service.patrocinador;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorJaExisteException;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPatrocinadorMapper;
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
public class AdicionarPatrocinadorService {

    private final PatrocinadorRepository patrocinadorRepository;
    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final PatrocinadorValidator patrocinadorValidator;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarPatrocinadorResponse adicionar(AdicionarPatrocinadorRequest request) {

        patrocinadorValidator.validarPatrocinadorJaExistePorCnpj(request.getCnpj());

        Patrocinador novoPatrocinador = AdicionarPatrocinadorMapper.toEntity(request);

        patrocinadorRepository.save(novoPatrocinador);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.ADICIONOU_PATROCINADOR,
                EntidadeAuditoria.PATROCINADOR,
                novoPatrocinador.getId(),
                novoPatrocinador.getNome()
        );

        return AdicionarPatrocinadorMapper.toResponse(novoPatrocinador.getId());

    }

}

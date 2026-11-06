package br.com.planeta_atlantida.rbs.api.service.cortesia;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarCortesiaRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarCortesiaResponse;
import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarCortesiaMapper;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPatrocinadorMapper;
import br.com.planeta_atlantida.rbs.api.repository.CortesiaRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.validator.CortesiaValidator;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarCortesiaService {

    private final PatrocinioValidator patrocinioValidator;
    private final CortesiaValidator cortesiaValidator;
    private final CortesiaRepository cortesiaRepository;
    private final RegistrarAuditoriaService registrarAuditoriaService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarCortesiaResponse adicionar(Long idPatrocinio, AdicionarCortesiaRequest request) {

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);

        cortesiaValidator.validarCortesiaJaExiste(patrocinio.getId());

        Cortesia novaCortesia = AdicionarCortesiaMapper.toEntity(patrocinio, request);

        cortesiaRepository.save(novaCortesia);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.ADICIONOU_CORTESIA,
                EntidadeAuditoria.CORTESIA,
                novaCortesia.getId(),
                patrocinio.getPatrocinador().getNome()
        );

        return AdicionarCortesiaMapper.toResponse(novaCortesia);

    }

}

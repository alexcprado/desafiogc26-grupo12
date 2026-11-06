package br.com.planeta_atlantida.rbs.api.service.cortesia;

import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
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
public class RemoverCortesiaService {

    private final CortesiaValidator cortesiaValidator;
    private final CortesiaRepository cortesiaRepository;
    private final PatrocinioValidator patrocinioValidator;
    private final RegistrarAuditoriaService registrarAuditoriaService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Long remover(Long idPatrocinio, Long idCortesia) {

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);

        Cortesia cortesia = cortesiaValidator.validarCortesiaExiste(idCortesia, patrocinio.getId());

        cortesiaRepository.delete(cortesia);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.REMOVEU_CORTESIA,
                EntidadeAuditoria.CORTESIA,
                idCortesia,
                patrocinio.getPatrocinador().getNome()
        );

        return idCortesia;

    }

}

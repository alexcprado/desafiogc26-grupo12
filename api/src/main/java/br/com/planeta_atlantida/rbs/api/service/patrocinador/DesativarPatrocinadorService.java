package br.com.planeta_atlantida.rbs.api.service.patrocinador;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.DesativarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.StatusPatrocinador;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinadorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DesativarPatrocinadorService {

    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final PatrocinadorValidator patrocinadorValidator;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DesativarPatrocinadorResponse desativar(Long idPatrocinador) {

        Patrocinador patrocinador = patrocinadorValidator.validarPatrocinadorExiste(idPatrocinador);

        patrocinador.setStatus(StatusPatrocinador.INATIVO);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.DESATIVOU_PATROCINADOR,
                EntidadeAuditoria.PATROCINADOR,
                patrocinador.getId(),
                patrocinador.getNome()
        );

        return DesativarPatrocinadorResponse.builder()
                .idPatrocinador(idPatrocinador).build();

    }

}

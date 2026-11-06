package br.com.planeta_atlantida.rbs.api.service.patrocinador;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.mapper.BuscarPatrocinadorMapper;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinadorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuscarPatrocinadorPorIdService {

    private final PatrocinadorValidator patrocinadorValidator;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public PatrocinadorResponse buscar(Long id) {

        Patrocinador patrocinador = patrocinadorValidator.validarPatrocinadorExiste(id);

        return BuscarPatrocinadorMapper.toResponse(patrocinador);

    }

}

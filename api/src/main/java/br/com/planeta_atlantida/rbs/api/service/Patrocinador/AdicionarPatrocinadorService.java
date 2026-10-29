package br.com.planeta_atlantida.rbs.api.service.Patrocinador;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarPatrocinadorMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarPatrocinadorService {

    private final PatrocinadorRepository patrocinadorRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarPatrocinadorResponse adicionar(AdicionarPatrocinadorRequest request) {

        Patrocinador novoPatrocinador = AdicionarPatrocinadorMapper.toEntity(request);

        patrocinadorRepository.save(novoPatrocinador);

        return AdicionarPatrocinadorMapper.toResponse(novoPatrocinador.getId());

    }

}

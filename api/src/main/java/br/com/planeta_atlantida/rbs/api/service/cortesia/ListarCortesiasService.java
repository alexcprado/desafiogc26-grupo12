package br.com.planeta_atlantida.rbs.api.service.cortesia;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarCortesiasResponse;
import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.mapper.ListarCortesiaMapper;
import br.com.planeta_atlantida.rbs.api.repository.CortesiaRepository;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarCortesiasService {

    private final CortesiaRepository cortesiaRepository;
    private final PatrocinioValidator patrocinioValidator;

    public List<ListarCortesiasResponse> listar(Long idPatrocinio) {

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);

        patrocinioValidator.validarPermissao(patrocinio);

        List<Cortesia> cortesias = cortesiaRepository.findByPatrocinio_Id(idPatrocinio);

        return cortesias.stream()
                .map(ListarCortesiaMapper::toResponse)
                .toList();

    }

}

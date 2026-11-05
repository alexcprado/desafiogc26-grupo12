package br.com.planeta_atlantida.rbs.api.service.patrocinio;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.DetalhesPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.mapper.DetalhesPatrocinioMapper;
import br.com.planeta_atlantida.rbs.api.repository.DocumentoRepository;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarDetalhesPatrocinioService {

    private final PatrocinioValidator patrocinioValidator;
    private final DocumentoRepository documentoRepository;

    @PreAuthorize("hasAnyRole('ADMIN', 'PATROCINADOR')")
    @Transactional(readOnly = true)
    public DetalhesPatrocinioResponse buscar(Long idPatrocinio) {

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);

        patrocinioValidator.validarPermissao(patrocinio);

        List<Documento> documentos = documentoRepository.findByPatrocinio_IdOrderByDataHoraCriacaoDesc(idPatrocinio);

        return DetalhesPatrocinioMapper.toResponse(patrocinio, documentos);

    }

}

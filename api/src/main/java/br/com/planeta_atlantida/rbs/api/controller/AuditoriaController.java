package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarAuditoriaResponse;
import br.com.planeta_atlantida.rbs.api.service.auditoria.ListarAuditoriasService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final ListarAuditoriasService listarAuditoriasService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<Page<ListarAuditoriaResponse>> listar(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(listarAuditoriasService.listar(pageable));
    }

}

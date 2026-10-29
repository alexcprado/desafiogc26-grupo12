package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPatrocinadoresResponse;
import br.com.planeta_atlantida.rbs.api.service.Patrocinador.AdicionarPatrocinadorService;
import br.com.planeta_atlantida.rbs.api.service.Patrocinador.ListarPatrocinadoresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patrocinadores")
@RequiredArgsConstructor
public class PatrocinadorController {

    private final ListarPatrocinadoresService listarPatrocinadoresService;
    private final AdicionarPatrocinadorService adicionarPatrocinadorService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<Page<ListarPatrocinadoresResponse>> listar(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(listarPatrocinadoresService.listar(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<AdicionarPatrocinadorResponse> adicionar(@RequestBody @Valid AdicionarPatrocinadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarPatrocinadorService.adicionar(request));
    }

}

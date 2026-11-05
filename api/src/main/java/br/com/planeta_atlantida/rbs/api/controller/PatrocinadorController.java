package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AlterarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AlterarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.DesativarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPatrocinadoresResponse;
import br.com.planeta_atlantida.rbs.api.service.patrocinador.AdicionarPatrocinadorService;
import br.com.planeta_atlantida.rbs.api.service.patrocinador.AlterarPatrocinadorService;
import br.com.planeta_atlantida.rbs.api.service.patrocinador.DesativarPatrocinadorService;
import br.com.planeta_atlantida.rbs.api.service.patrocinador.ListarPatrocinadoresService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
    private final AlterarPatrocinadorService alterarPatrocinadorService;
    private final DesativarPatrocinadorService desativarPatrocinadorService;

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

    @PutMapping("/{id}")
    public ResponseEntity<AlterarPatrocinadorResponse> alterar(
            @PathVariable Long id,
            @RequestBody @Valid AlterarPatrocinadorRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(alterarPatrocinadorService.alterar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DesativarPatrocinadorResponse> desativar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(desativarPatrocinadorService.desativar(id));
    }


}

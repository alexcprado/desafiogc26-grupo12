package br.com.planeta_atlantida.rbs.api.controller;


import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinioRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BuscarEventoResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinioResumoResponse;
import br.com.planeta_atlantida.rbs.api.service.evento.BuscarEventoPorIdService;
import br.com.planeta_atlantida.rbs.api.service.evento.ListarEventosService;
import br.com.planeta_atlantida.rbs.api.service.patrocinador.AdicionarPatrocinadorService;
import br.com.planeta_atlantida.rbs.api.service.patrocinio.AdicionarPatrocinioService;
import br.com.planeta_atlantida.rbs.api.service.patrocinio.ListarPatrociniosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final BuscarEventoPorIdService buscarEventoPorIdService;
    private final ListarEventosService listarEventosService;
    private final ListarPatrociniosService listarPatrociniosService;
    private final AdicionarPatrocinadorService criarPatrocinadorService;
    private final AdicionarPatrocinioService adicionarPatrocinioService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<Page<BuscarEventoResponse>> listar(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(listarEventosService.listar(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BuscarEventoResponse> buscarEvento(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(buscarEventoPorIdService.buscar(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/patrocinios")
    public ResponseEntity<Page<PatrocinioResumoResponse>> listarPatrocinios(
            @PathVariable Long id,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(listarPatrociniosService.listar(id, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/patrocinadores")
    public ResponseEntity<AdicionarPatrocinadorResponse> criarPatrocinador(
            @PathVariable Long id,
            @RequestBody @Valid AdicionarPatrocinadorRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarPatrocinadorService.adicionar(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/patrocinios")
    public ResponseEntity<AdicionarPatrocinioResponse> adicionarPatrocinio(
            @PathVariable Long id,
            @RequestBody @Valid AdicionarPatrocinioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarPatrocinioService.adicionar(id, request));
    }

}

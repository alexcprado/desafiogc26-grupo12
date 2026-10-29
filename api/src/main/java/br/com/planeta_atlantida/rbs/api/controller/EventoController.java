package br.com.planeta_atlantida.rbs.api.controller;


import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BuscarEventoResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinadorResponse;
import br.com.planeta_atlantida.rbs.api.service.Evento.BuscarEventoPorIdService;
import br.com.planeta_atlantida.rbs.api.service.Patrocinador.AdicionarPatrocinadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final BuscarEventoPorIdService buscarEventoPorIdService;
    private final AdicionarPatrocinadorService criarPatrocinadorService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BuscarEventoResponse> buscarEvento(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(buscarEventoPorIdService.buscar(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/patrocinadores")
    public ResponseEntity<AdicionarPatrocinadorResponse> criarPatrocinador(
            @PathVariable Long id,
            @RequestBody @Valid AdicionarPatrocinadorRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(criarPatrocinadorService.criar(request));
    }

}

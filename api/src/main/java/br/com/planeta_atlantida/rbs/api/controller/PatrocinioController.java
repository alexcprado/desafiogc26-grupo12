package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPesquisaAtivacaoRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPesquisaAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.DetalhesPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPesquisasAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.service.patrocinio.BuscarDetalhesPatrocinioService;
import br.com.planeta_atlantida.rbs.api.service.pesquisaativacao.AdicionarPesquisaAtivacaoService;
import br.com.planeta_atlantida.rbs.api.service.pesquisaativacao.ListarPesquisasAtivacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patrocinios")
@RequiredArgsConstructor
public class PatrocinioController {

    private final AdicionarPesquisaAtivacaoService adicionarPesquisaAtivacaoService;
    private final ListarPesquisasAtivacaoService listarPesquisasAtivacaoService;
    private final BuscarDetalhesPatrocinioService buscarDetalhesPatrocinioService;

    @GetMapping("/{id}/detalhes")
    public ResponseEntity<DetalhesPatrocinioResponse> buscarDetalhes(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(buscarDetalhesPatrocinioService.buscar(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pesquisa-ativacao")
    public ResponseEntity<Page<ListarPesquisasAtivacaoResponse>> listar(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(listarPesquisasAtivacaoService.listar(pageable));
    }

    @PostMapping("/{id}/pesquisa-ativacao")
    public ResponseEntity<AdicionarPesquisaAtivacaoResponse> adicionar(
            @PathVariable Long id,
            @RequestBody @Valid AdicionarPesquisaAtivacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarPesquisaAtivacaoService.adicionar(id, request));
    }

}

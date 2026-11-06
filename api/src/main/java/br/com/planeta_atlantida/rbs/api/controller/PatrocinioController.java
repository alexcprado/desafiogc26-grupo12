package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarCortesiaRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPesquisaAtivacaoRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.*;
import br.com.planeta_atlantida.rbs.api.service.cortesia.AdicionarCortesiaService;
import br.com.planeta_atlantida.rbs.api.service.cortesia.ListarCortesiasService;
import br.com.planeta_atlantida.rbs.api.service.cortesia.RemoverCortesiaService;
import br.com.planeta_atlantida.rbs.api.service.patrocinio.BuscarDetalhesPatrocinioService;
import br.com.planeta_atlantida.rbs.api.service.pesquisaativacao.AdicionarPesquisaAtivacaoService;
import br.com.planeta_atlantida.rbs.api.service.pesquisaativacao.ListarPesquisasAtivacaoService;
import br.com.planeta_atlantida.rbs.api.service.relatorio.GerarRelatorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/patrocinios")
@RequiredArgsConstructor
public class PatrocinioController {

    private final AdicionarPesquisaAtivacaoService adicionarPesquisaAtivacaoService;
    private final ListarPesquisasAtivacaoService listarPesquisasAtivacaoService;
    private final BuscarDetalhesPatrocinioService buscarDetalhesPatrocinioService;
    private final AdicionarCortesiaService adicionarCortesiaService;
    private final RemoverCortesiaService removerCortesiaService;
    private final ListarCortesiasService listarCortesiasService;
    private final GerarRelatorioService gerarRelatorioService;

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

    @GetMapping("/{id}/cortesias")
    public ResponseEntity<List<ListarCortesiasResponse>> listar(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(listarCortesiasService.listar(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/cortesias")
    public ResponseEntity<AdicionarCortesiaResponse> adicionar(
            @PathVariable Long id,
            @RequestBody @Valid AdicionarCortesiaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarCortesiaService.adicionar(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/cortesias/{idCortesia}")
    public ResponseEntity<Long> remover(
            @PathVariable Long id,
            @PathVariable Long idCortesia) {
        return ResponseEntity.status(HttpStatus.OK).body(removerCortesiaService.remover(id, idCortesia));
    }

    @GetMapping(path = "/{id}/relatorio", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> gerar(@PathVariable Long id) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(gerarRelatorioService.gerar(id));
    }

}

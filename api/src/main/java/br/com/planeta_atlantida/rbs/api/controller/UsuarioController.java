package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.service.Usuario.AdicionarUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final AdicionarUsuarioService adicionarUsuarioService;

    @PostMapping("")
    public ResponseEntity<AdicionarUsuarioResponse> adicionar(@Valid @RequestBody AdicionarUsuarioRequest request) {
        return ResponseEntity.ok(adicionarUsuarioService.adicionar(request));
    }

}

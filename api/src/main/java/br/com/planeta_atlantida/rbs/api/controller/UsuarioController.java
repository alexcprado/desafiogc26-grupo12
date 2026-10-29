package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioAdminRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.service.Usuario.AdicionarUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final AdicionarUsuarioService adicionarUsuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admins")
    public ResponseEntity<AdicionarUsuarioResponse> adicionarUsuarioAdmin(@Valid @RequestBody AdicionarUsuarioAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarUsuarioService.adicionarAdmin(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/patrocinadores")
    public ResponseEntity<AdicionarUsuarioResponse> adicionarUsuarioPatrocinador(@Valid @RequestBody AdicionarUsuarioPatrocinadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adicionarUsuarioService.adicionarPatrocinador(request));
    }

}

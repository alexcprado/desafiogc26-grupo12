package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.LoginRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.LoginResponse;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.exception.BadCredentialsException;
import br.com.planeta_atlantida.rbs.api.service.Usuario.BuscarUsuarioPorEmailService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BuscarUsuarioPorEmailService buscarUsuarioPorEmailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    private boolean isLoginCorreto(String password, String savedPassword) {
        return passwordEncoder.matches(password, savedPassword);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<Usuario> optUser = buscarUsuarioPorEmailService.buscarPorEmail(loginRequest.getEmail());

        if (optUser.isEmpty() || !isLoginCorreto(loginRequest.getSenha(), optUser.get().getSenha())) {
            throw new BadCredentialsException(Mensagens.USUARIO_OU_SENHA_INCORRETOS);
        }

        Usuario usuario = optUser.get();
        long expiresIn = 600L;

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("planeta-atlantida-api")
                .subject(usuario.getNome())
                .expiresAt(Instant.now().plusSeconds(expiresIn))
                .issuedAt(Instant.now())
                .claim("userId", usuario.getId())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(token)
                .expiresIn(expiresIn)
                .build();

        return ResponseEntity.ok(loginResponse);
    }
}

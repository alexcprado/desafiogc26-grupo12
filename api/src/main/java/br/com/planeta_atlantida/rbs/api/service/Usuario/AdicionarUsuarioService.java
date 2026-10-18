package br.com.planeta_atlantida.rbs.api.service.Usuario;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarUsuarioMapper;
import br.com.planeta_atlantida.rbs.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdicionarUsuarioResponse adicionar(AdicionarUsuarioRequest request) {

        Usuario novoUsuario = AdicionarUsuarioMapper.toEntity(request);

        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));

        novoUsuario.setTipo(TipoUsuario.ADMIN);

        usuarioRepository.save(novoUsuario);

        return AdicionarUsuarioMapper.toResponse(novoUsuario);

    }

}

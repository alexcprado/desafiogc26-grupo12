package br.com.planeta_atlantida.rbs.api.service.usuario;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioAdminRequest;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarUsuarioMapper;
import br.com.planeta_atlantida.rbs.api.repository.UsuarioRepository;
import br.com.planeta_atlantida.rbs.api.utils.Emails;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinadorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PatrocinadorValidator patrocinadorValidator;
    private final PasswordEncoder passwordEncoder;


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarUsuarioResponse adicionarAdmin(AdicionarUsuarioAdminRequest request) {

        Usuario novoUsuario = AdicionarUsuarioMapper.toEntity(request);
        novoUsuario.setTipo(TipoUsuario.ADMIN);

        return salvar(novoUsuario);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public AdicionarUsuarioResponse adicionarPatrocinador(AdicionarUsuarioPatrocinadorRequest request) {

        Patrocinador patrocinador = patrocinadorValidator.validarPatrocinadorExiste(request.getIdPatrocinador());

        Usuario novoUsuario = AdicionarUsuarioMapper.toEntity(request);

        novoUsuario.setPatrocinador(patrocinador);
        novoUsuario.setTipo(TipoUsuario.PATROCINADOR);

        return salvar(novoUsuario);

    }

    private AdicionarUsuarioResponse salvar(Usuario usuario) {

        usuario.setEmail(Emails.normalizar(usuario.getEmail()));
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return AdicionarUsuarioMapper.toResponse(usuarioSalvo);

    }

}

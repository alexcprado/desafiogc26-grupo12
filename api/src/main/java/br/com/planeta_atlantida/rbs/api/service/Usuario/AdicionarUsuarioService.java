package br.com.planeta_atlantida.rbs.api.service.Usuario;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarAdminRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.mapper.AdicionarUsuarioMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import br.com.planeta_atlantida.rbs.api.repository.UsuarioRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PatrocinadorRepository patrocinadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdicionarUsuarioResponse adicionarAdmin(AdicionarAdminRequest request) {

        Usuario novoUsuario = AdicionarUsuarioMapper.toEntity(request);
        novoUsuario.setTipo(TipoUsuario.ADMIN);

        return salvar(novoUsuario);

    }

    @Transactional
    public AdicionarUsuarioResponse adicionarPatrocinador(AdicionarPatrocinadorRequest request) {

        Patrocinador patrocinador = patrocinadorRepository
                .findById(request.getIdPatrocinador())
                .orElseThrow(() ->
                        new PatrocinadorNaoEncontradoException(
                                Mensagens.PATROCINADOR_NAO_ENCONTRADO
                        ));

        Usuario novoUsuario = AdicionarUsuarioMapper.toEntity(request);

        novoUsuario.setPatrocinador(patrocinador);
        novoUsuario.setTipo(TipoUsuario.PATROCINADOR);

        return salvar(novoUsuario);

    }

    private AdicionarUsuarioResponse salvar(Usuario usuario) {

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        return AdicionarUsuarioMapper.toResponse(usuarioSalvo);

    }

}

package br.com.planeta_atlantida.rbs.api.service.usuario;

import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.exception.TokenSemIdentificacaoException;
import br.com.planeta_atlantida.rbs.api.exception.UsuarioNaoAutenticadoException;
import br.com.planeta_atlantida.rbs.api.repository.UsuarioRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarUsuarioLogadoService {

    private final UsuarioRepository usuarioRepository;

    public Usuario buscar() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            throw new UsuarioNaoAutenticadoException(Mensagens.USUARIO_NAO_AUTENTICADO);
        }

        String subject = jwtAuthentication.getToken().getSubject();

        if (subject == null || subject.isBlank()) {
            throw new TokenSemIdentificacaoException(Mensagens.TOKEN_SEM_IDENTIFICACAO);
        }

        return usuarioRepository.findByEmail(subject)
                .orElseThrow(() ->
                        new UsuarioNaoAutenticadoException(Mensagens.USUARIO_NAO_ENCONTRADO));
    }

}

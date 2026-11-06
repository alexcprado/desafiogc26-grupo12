package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.UsuarioLogadoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioLogadoMapper {

    public static UsuarioLogadoResponse toResponse(Usuario usuario) {
        return UsuarioLogadoResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .tipo(usuario.getTipo())
                .idPatrocinador(usuario.getPatrocinador() != null
                        ? usuario.getPatrocinador().getId()
                        : null)
                .nomePatrocinador(usuario.getPatrocinador() != null
                        ? usuario.getPatrocinador().getNome()
                        : null)
                .build();
    }

}

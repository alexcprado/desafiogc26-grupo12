package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioAdminRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AdicionarUsuarioMapper {

    public static Usuario toEntity(AdicionarUsuarioAdminRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .build();
    }

    public static Usuario toEntity(AdicionarUsuarioPatrocinadorRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .build();
    }

    public static AdicionarUsuarioResponse toResponse(Usuario usuario) {
        return AdicionarUsuarioResponse.builder()
                .id(usuario.getId())
                .build();
    }

}

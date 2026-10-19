package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarAdminRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import org.springframework.stereotype.Component;

@Component
public class AdicionarUsuarioMapper {

    public static Usuario toEntity(AdicionarAdminRequest request) {
        return Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(request.getSenha())
                .build();
    }

    public static Usuario toEntity(AdicionarPatrocinadorRequest request) {
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

package br.com.planeta_atlantida.rbs.api.controller.dtos.response;

import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLogadoResponse {

    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipo;
    private Long idPatrocinador;
    private String nomePatrocinador;

}

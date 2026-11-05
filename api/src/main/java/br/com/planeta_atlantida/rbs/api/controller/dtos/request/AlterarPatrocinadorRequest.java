package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlterarPatrocinadorRequest {

    @NotBlank(message = "Nome deve ser informado.")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "Segmento deve ser informado.")
    private String segmento;

    @NotBlank(message = "Contato principal deve ser informado.")
    private String contatoPrincipal;

    @NotBlank(message = "Numero de telefone deve ser informado.")
    private String telefone;

}

package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import jakarta.validation.constraints.Email;
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
public class AdicionarUsuarioRequest {

    @NotBlank(message = "Nome deve ser informado.")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "Email deve ser informado.")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha deve ser informada.")
    private String senha;

}

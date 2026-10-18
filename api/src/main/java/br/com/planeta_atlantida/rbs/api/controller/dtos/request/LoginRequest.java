package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Email deve ser informado.")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha deve ser informada.")
    private String senha;
}

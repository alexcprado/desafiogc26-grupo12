package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarPatrocinadorRequest {

    @NotBlank(message = "Nome deve ser informado.")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "CNPJ deve ser informado.")
    @CNPJ(message = "CNPJ deve ser válido.")
    private String cnpj;

}

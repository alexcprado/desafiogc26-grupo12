package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarPesquisaAtivacaoRequest {

    @NotBlank(message = "A primeira pergunta de ativação deve ser informada.")
    private String pergunta1;

    @NotBlank(message = "A segunda pergunta de ativação deve ser informada.")
    private String pergunta2;

    @NotBlank(message = "A terceira pergunta de ativação deve ser informada.")
    private String pergunta3;

}

package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import br.com.planeta_atlantida.rbs.api.enums.SetorCortesia;
import br.com.planeta_atlantida.rbs.api.enums.TipoCortesia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarCortesiaRequest {

    @NotNull(message = "Setor da cortesia deve ser informado.")
    private SetorCortesia setor;

    @NotNull(message = "Tipo da cortesia deve ser informado.")
    private TipoCortesia tipo;

    @NotNull(message = "Quantidade de cortesias deve ser informado.")
    @PositiveOrZero(message = "Quantidade deve ser um valor positivo.")
    private Integer quantidade;

}

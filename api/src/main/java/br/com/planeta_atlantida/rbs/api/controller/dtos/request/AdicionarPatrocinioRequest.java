package br.com.planeta_atlantida.rbs.api.controller.dtos.request;

import br.com.planeta_atlantida.rbs.api.enums.CotaPatrocinio;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarPatrocinioRequest {

    @NotNull(message = "Id do patrocinador deve ser informado.")
    private Long idPatrocinador;

    @NotNull(message = "Tipo de cota do patrocinador deve ser informado.")
    private CotaPatrocinio cota;

}

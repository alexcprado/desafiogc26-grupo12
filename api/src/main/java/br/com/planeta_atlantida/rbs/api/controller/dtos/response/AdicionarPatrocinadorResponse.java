package br.com.planeta_atlantida.rbs.api.controller.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdicionarPatrocinadorResponse {

    private Long idPatrocinador;

}

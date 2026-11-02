package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarPesquisasAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.domain.PesquisaAtivacao;
import org.springframework.stereotype.Component;

@Component
public class ListarPesquisaAtivacaoMapper {

    public static ListarPesquisasAtivacaoResponse toResponse(PesquisaAtivacao entity) {
        return ListarPesquisasAtivacaoResponse.builder()
                .id(entity.getId())
                .idPatrocinio(entity.getPatrocinio().getId())
                .pergunta1(entity.getPergunta1())
                .pergunta2(entity.getPergunta2())
                .pergunta3(entity.getPergunta3())
                .build();
    }

}

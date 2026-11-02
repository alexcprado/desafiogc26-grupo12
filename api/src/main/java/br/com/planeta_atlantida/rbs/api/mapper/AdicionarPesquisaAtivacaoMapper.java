package br.com.planeta_atlantida.rbs.api.mapper;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPesquisaAtivacaoRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPesquisaAtivacaoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.PesquisaAtivacao;
import org.springframework.stereotype.Component;

@Component
public class AdicionarPesquisaAtivacaoMapper {

    public static PesquisaAtivacao toEntity(AdicionarPesquisaAtivacaoRequest request, Patrocinio patrocinio) {
        return PesquisaAtivacao.builder()
                .patrocinio(patrocinio)
                .pergunta1(request.getPergunta1())
                .pergunta2(request.getPergunta2())
                .pergunta3(request.getPergunta3())
                .build();
    }

    public static AdicionarPesquisaAtivacaoResponse toResponse(Long id) {
        return AdicionarPesquisaAtivacaoResponse.builder()
                .id(id)
                .build();
    }

}

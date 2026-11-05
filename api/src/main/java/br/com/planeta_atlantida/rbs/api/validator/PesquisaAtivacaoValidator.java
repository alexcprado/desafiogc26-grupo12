package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.enums.CotaPatrocinio;
import br.com.planeta_atlantida.rbs.api.exception.CotaNaoPermitidaException;
import br.com.planeta_atlantida.rbs.api.exception.PesquisaAtivacaoJaExisteException;
import br.com.planeta_atlantida.rbs.api.repository.PesquisaAtivacaoRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PesquisaAtivacaoValidator {

    private final PesquisaAtivacaoRepository pesquisaAtivacaoRepository;

    public void validarPesquisaAtivacaoJaExiste(Long idPatrocinio) {
        if (pesquisaAtivacaoRepository.existsByPatrocinio_Id(idPatrocinio)) {
            throw new PesquisaAtivacaoJaExisteException(Mensagens.PESQUISA_ATIVACAO_JA_EXISTE);
        }
    }

    public void validarCotaMaster(Patrocinio patrocinio) {
        if (patrocinio.getCota() != CotaPatrocinio.MASTER) {
            throw new CotaNaoPermitidaException(Mensagens.COTA_NAO_PERMITE_PESQUISA_ATIVACAO);
        }
    }

}

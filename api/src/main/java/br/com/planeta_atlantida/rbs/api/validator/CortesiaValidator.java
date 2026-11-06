package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.domain.Cortesia;
import br.com.planeta_atlantida.rbs.api.exception.CortesiaJaCadastradaException;
import br.com.planeta_atlantida.rbs.api.exception.CortesiaNaoEncontradaException;
import br.com.planeta_atlantida.rbs.api.repository.CortesiaRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CortesiaValidator {

    private final CortesiaRepository cortesiaRepository;

    public void validarCortesiaJaExiste(Long idPatrocinio) {
        if (cortesiaRepository.existsByPatrocinio_Id(idPatrocinio)) {
            throw new CortesiaJaCadastradaException(Mensagens.CORTESIA_JA_CADASTRADA);
        }
    }

    public Cortesia validarCortesiaExiste(Long idCortesia, Long idPatrocinio) {
        return cortesiaRepository.findByIdAndPatrocinio_Id(idCortesia, idPatrocinio)
                .orElseThrow(() ->
                        new CortesiaNaoEncontradaException(Mensagens.CORTESIA_NAO_ENCONTRADA
                        ));
    }

}

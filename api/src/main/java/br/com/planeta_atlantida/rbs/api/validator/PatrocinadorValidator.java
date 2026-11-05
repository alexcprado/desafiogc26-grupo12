package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinador;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorJaExisteException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinadorRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatrocinadorValidator {

    private final PatrocinadorRepository patrocinadorRepository;

    public Patrocinador validarPatrocinadorExiste(Long idPatrocinador) {
        return patrocinadorRepository
                .findById(idPatrocinador)
                .orElseThrow(() ->
                        new PatrocinadorNaoEncontradoException(
                                Mensagens.PATROCINADOR_NAO_ENCONTRADO
                        ));
    }

    public void validarPatrocinadorJaExistePorCnpj(String cnpj) {
        if (patrocinadorRepository.existsByCnpj(cnpj)) {
            throw new PatrocinadorJaExisteException(Mensagens.PATROCINADOR_JA_EXISTE);
        }
    }

}

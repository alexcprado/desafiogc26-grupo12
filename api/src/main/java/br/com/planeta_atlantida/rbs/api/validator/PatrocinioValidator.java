package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinadorJaVinculadoException;
import br.com.planeta_atlantida.rbs.api.exception.PatrocinioNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.exception.SemPermissaoException;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PatrocinioValidator {

    private final PatrocinioRepository patrocinioRepository;
    private final BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    public Patrocinio validarPatrocinioExiste(Long idPatrocinio) {
        return patrocinioRepository.findById(idPatrocinio)
                .orElseThrow(() ->
                        new PatrocinioNaoEncontradoException(Mensagens.PATROCINIO_NAO_ENCONTRADO
                        ));
    }

    public void validarPermissao(Patrocinio patrocinio) {

        Usuario usuario = buscarUsuarioLogadoService.buscar();

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            return;
        }

        if (usuario.getTipo() != TipoUsuario.PATROCINADOR
                || usuario.getPatrocinador() == null
                || !Objects.equals(
                usuario.getPatrocinador().getId(),
                patrocinio.getPatrocinador().getId()
        )) {
            throw new SemPermissaoException(Mensagens.SEM_PERMISSAO_PARA_EXECUTAR_ACAO);
        }

    }

    public void validarPatrocinadorJaVinculado(Long idEvento, Long idPatrocinador) {
        if (patrocinioRepository.existsByEvento_IdAndPatrocinador_Id(idEvento, idPatrocinador)) {
            throw new PatrocinadorJaVinculadoException(Mensagens.PATROCINADOR_JA_VINCULADO);
        }
    }

}

package br.com.planeta_atlantida.rbs.api.service.patrocinio;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinioResumoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.exception.SemPermissaoException;
import br.com.planeta_atlantida.rbs.api.mapper.PatrocinioResumoMapper;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarMeusPatrociniosService {

    private final PatrocinioRepository patrocinioRepository;
    private final BuscarUsuarioLogadoService buscarUsuarioLogadoService;

    @PreAuthorize("hasRole('PATROCINADOR')")
    @Transactional(readOnly = true)
    public List<PatrocinioResumoResponse> listar() {

        Usuario usuario = buscarUsuarioLogadoService.buscar();

        if (usuario.getPatrocinador() == null) {
            throw new SemPermissaoException(Mensagens.SEM_PERMISSAO_PARA_EXECUTAR_ACAO);
        }

        return patrocinioRepository
                .findByPatrocinador_IdOrderByEvento_AnoDesc(usuario.getPatrocinador().getId())
                .stream()
                .map(PatrocinioResumoMapper::toResponse)
                .toList();

    }

}

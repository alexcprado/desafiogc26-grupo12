package br.com.planeta_atlantida.rbs.api.service.documento;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BaixarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.TipoUsuario;
import br.com.planeta_atlantida.rbs.api.exception.ArquivoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.exception.SemPermissaoParaBaixarDocumentoException;
import br.com.planeta_atlantida.rbs.api.exception.SemPermissaoParaEnviarDocumentoException;
import br.com.planeta_atlantida.rbs.api.mapper.BaixarDocumentoMapper;
import br.com.planeta_atlantida.rbs.api.repository.DocumentoRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BaixarDocumentoService {

    private final DocumentoRepository documentoRepository;
    private final BuscarUsuarioLogadoService buscarUsuarioLogadoService;
    private final ArmazenamentoDocumentoService armazenamentoDocumentoService;
    private final RegistrarAuditoriaService registrarAuditoriaService;

    @Transactional
    public BaixarDocumentoResponse baixar(Long idDocumento) {

        Documento documento = documentoRepository.findById(idDocumento)
                .orElseThrow(() ->
                        new ArquivoNaoEncontradoException(Mensagens.ARQUIVO_NAO_ENCONTRADO
                        ));

        Usuario usuario = buscarUsuarioLogadoService.buscar();

        validarPermissao(usuario, documento.getPatrocinio());

    try {

        Resource arquivo = armazenamentoDocumentoService.carregar(documento.getChaveArmazenamento());

        registrarAuditoriaService.registrar(
                AcaoAuditoria.BAIXOU_DOCUMENTO,
                EntidadeAuditoria.DOCUMENTO,
                documento.getId(),
                documento.getNomeArquivo()
        );

        return BaixarDocumentoMapper.toResponse(arquivo, documento);

    } catch (IOException exception) {
        throw new ArquivoNaoEncontradoException(Mensagens.ARQUIVO_NAO_ENCONTRADO);
    }

    }

    private void validarPermissao(Usuario usuario, Patrocinio patrocinio) {

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            return;
        }

        if (usuario.getTipo() != TipoUsuario.PATROCINADOR
                || usuario.getPatrocinador() == null
                || !Objects.equals(
                usuario.getPatrocinador().getId(),
                patrocinio.getPatrocinador().getId()
        )) {
            throw new SemPermissaoParaBaixarDocumentoException(Mensagens.SEM_PERMISSAO_PARA_BAIXAR_ARQUIVO);
        }

    }

}

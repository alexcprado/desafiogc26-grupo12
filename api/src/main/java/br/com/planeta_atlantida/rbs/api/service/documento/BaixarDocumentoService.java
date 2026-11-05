package br.com.planeta_atlantida.rbs.api.service.documento;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BaixarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.enums.AcaoAuditoria;
import br.com.planeta_atlantida.rbs.api.enums.EntidadeAuditoria;
import br.com.planeta_atlantida.rbs.api.exception.ArquivoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.mapper.BaixarDocumentoMapper;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.DocumentoValidator;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BaixarDocumentoService {

    private final ArmazenamentoDocumentoService armazenamentoDocumentoService;
    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final DocumentoValidator documentoValidator;
    private final PatrocinioValidator patrocinioValidator;

    @Transactional
    public BaixarDocumentoResponse baixar(Long idDocumento) {

        Documento documento = documentoValidator.validarDocumentoExiste(idDocumento);

        patrocinioValidator.validarPermissao(documento.getPatrocinio());

        try {

            Resource arquivo = armazenamentoDocumentoService.carregar(documento.getChaveArmazenamento());

            registrarAuditoriaService.registrar(
                    AcaoAuditoria.BAIXOU_DOCUMENTO,
                    EntidadeAuditoria.DOCUMENTO,
                    documento.getId(),
                    documento.getNomeArquivo()
            );

            return BaixarDocumentoMapper.toResponse(arquivo, documento);

        } catch (ArquivoNaoEncontradoException exception) {
            throw new ArquivoNaoEncontradoException(Mensagens.ARQUIVO_NAO_ENCONTRADO);
        }

    }

}

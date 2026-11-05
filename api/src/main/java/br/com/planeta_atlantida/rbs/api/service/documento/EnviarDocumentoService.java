package br.com.planeta_atlantida.rbs.api.service.documento;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.EnviarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.domain.Patrocinio;
import br.com.planeta_atlantida.rbs.api.domain.Usuario;
import br.com.planeta_atlantida.rbs.api.enums.*;
import br.com.planeta_atlantida.rbs.api.exception.*;
import br.com.planeta_atlantida.rbs.api.mapper.EnviarDocumentoMapper;
import br.com.planeta_atlantida.rbs.api.repository.DocumentoRepository;
import br.com.planeta_atlantida.rbs.api.repository.PatrocinioRepository;
import br.com.planeta_atlantida.rbs.api.service.auditoria.RegistrarAuditoriaService;
import br.com.planeta_atlantida.rbs.api.service.usuario.BuscarUsuarioLogadoService;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.DocumentoValidator;
import br.com.planeta_atlantida.rbs.api.validator.PatrocinioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EnviarDocumentoService {

    private final DocumentoRepository documentoRepository;
    private final BuscarUsuarioLogadoService buscarUsuarioLogadoService;
    private final ArmazenamentoDocumentoService armazenamentoDocumentoService;
    private final RegistrarAuditoriaService registrarAuditoriaService;
    private final PatrocinioValidator patrocinioValidator;
    private final DocumentoValidator documentoValidator;

    @Transactional
    public EnviarDocumentoResponse enviar(Long idPatrocinio, MultipartFile arquivo, CategoriaDocumento categoria) {

        documentoValidator.validarArquivo(arquivo);
        documentoValidator.validarCategoria(categoria);

        Usuario usuario = buscarUsuarioLogadoService.buscar();

        Patrocinio patrocinio = patrocinioValidator.validarPatrocinioExiste(idPatrocinio);
        patrocinioValidator.validarPermissao(patrocinio);
        documentoValidator.validarDocumentoJaExiste(idPatrocinio, categoria);

        String chaveArmazenamento;

        try {
            chaveArmazenamento = armazenamentoDocumentoService.armazenar(arquivo);
        } catch (IOException exception) {
            throw new ImpossivelArmazenarArquivoException(
                    Mensagens.IMPOSSIVEL_ARMAZENAR_ARQUIVO
            );
        }

        Documento novoDocumento = EnviarDocumentoMapper.toEntity(
                patrocinio,
                usuario,
                obterNomeArquivo(arquivo),
                categoria,
                chaveArmazenamento,
                obterTipoMime(arquivo),
                arquivo.getSize(),
                OrigemDocumento.valueOf(usuario.getTipo().name())
        );

        Documento documentoSalvo = documentoRepository.saveAndFlush(novoDocumento);

        registrarAuditoriaService.registrar(
                AcaoAuditoria.ADICIONOU_DOCUMENTO,
                EntidadeAuditoria.DOCUMENTO,
                documentoSalvo.getId(),
                documentoSalvo.getNomeArquivo()
        );

        return EnviarDocumentoMapper.toResponse(documentoSalvo.getId());

    }

    private String obterNomeArquivo(MultipartFile arquivo) {
        String nome = arquivo.getOriginalFilename();

        if (nome == null || nome.isBlank()) {
            return "documento";
        }

        return nome.replace("\\", "/")
                .substring(nome.replace("\\", "/").lastIndexOf("/") + 1);
    }

    private String obterTipoMime(MultipartFile arquivo) {
        String tipoMime = arquivo.getContentType();

        return tipoMime == null ||  tipoMime.isBlank()
                ? "application/octet-stream" : tipoMime;
    }

}

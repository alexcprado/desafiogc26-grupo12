package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.domain.Documento;
import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import br.com.planeta_atlantida.rbs.api.exception.ArquivoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.exception.ArquivoVazioException;
import br.com.planeta_atlantida.rbs.api.exception.CategoriaVaziaException;
import br.com.planeta_atlantida.rbs.api.exception.DocumentoJaExisteException;
import br.com.planeta_atlantida.rbs.api.repository.DocumentoRepository;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentoValidator {

    private final DocumentoRepository documentoRepository;

    public Documento validarDocumentoExiste(Long idDocumento) {
        return documentoRepository.findById(idDocumento)
                .orElseThrow(() ->
                        new ArquivoNaoEncontradoException(Mensagens.ARQUIVO_NAO_ENCONTRADO
                        ));
    }

    public void validarDocumentoJaExiste(Long idPatrocinio, CategoriaDocumento categoria) {
        if (documentoRepository.existsByPatrocinio_IdAndCategoria(idPatrocinio, categoria)) {
            throw new DocumentoJaExisteException(Mensagens.DOCUMENTO_JA_EXISTE);
        }
    }

    public void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ArquivoVazioException(Mensagens.ARQUIVO_VAZIO);
        }
    }

    public void validarCategoria(CategoriaDocumento categoria) {
        if (categoria == null) {
            throw new CategoriaVaziaException(Mensagens.CATEGORIA_VAZIA);
        }
    }

}

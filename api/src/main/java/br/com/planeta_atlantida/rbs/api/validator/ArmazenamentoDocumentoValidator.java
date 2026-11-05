package br.com.planeta_atlantida.rbs.api.validator;

import br.com.planeta_atlantida.rbs.api.exception.ArquivoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ArmazenamentoDocumentoValidator {

    public void verificarCaminho(Path destino, Path diretorio) throws IOException {
        if (!destino.getParent().equals(diretorio)) {
            throw new IOException(Mensagens.CAMINHO_ARMAZENAMENTO_INVALIDO);
        }
    }

    public void verificarArquivoExiste(Path arquivo) {
        if (!Files.isRegularFile(arquivo) || !Files.isReadable(arquivo)) {
            throw new ArquivoNaoEncontradoException(Mensagens.ARQUIVO_NAO_ENCONTRADO);
        }
    }

    public void verificarChaveArmazenamentoValida(Path arquivo, Path diretorio) throws IOException {
        if (!arquivo.getParent().equals(diretorio)) {
            throw new IOException(Mensagens.CHAVE_ARMAZENAMENTO_INVALIDA);
        }
    }

}

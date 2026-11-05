package br.com.planeta_atlantida.rbs.api.service.documento;

import br.com.planeta_atlantida.rbs.api.exception.ArquivoNaoEncontradoException;
import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import br.com.planeta_atlantida.rbs.api.validator.ArmazenamentoDocumentoValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ArmazenamentoDocumentoService {

    private final Path diretorio;
    private final ArmazenamentoDocumentoValidator armazenamentoDocumentoValidator;

    public ArmazenamentoDocumentoService(@Value("${app.storage.documentos.path}") String diretorio) {
        this.diretorio = Paths.get(diretorio).toAbsolutePath().normalize();
        this.armazenamentoDocumentoValidator = new ArmazenamentoDocumentoValidator();
    }


    public String armazenar(MultipartFile arquivo) throws IOException {

        Files.createDirectories(diretorio);

        String chave = UUID.randomUUID().toString();
        Path destino = diretorio.resolve(chave).normalize();

        armazenamentoDocumentoValidator.verificarCaminho(destino, diretorio);

        try (InputStream inputStream = arquivo.getInputStream()) {
            Files.copy(inputStream, destino);
        }

        return chave;

    }

    public Resource carregar(String chave) {

        Path arquivo = diretorio.resolve(chave).normalize();

        armazenamentoDocumentoValidator.verificarArquivoExiste(arquivo);

        return new FileSystemResource(arquivo);

    }

    public void remover(String chave) throws IOException {

        Path arquivo = diretorio.resolve(chave).normalize();

        armazenamentoDocumentoValidator.verificarChaveArmazenamentoValida(arquivo, diretorio);

        Files.deleteIfExists(arquivo);

    }

}

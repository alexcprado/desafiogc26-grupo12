package br.com.planeta_atlantida.rbs.api.service.documento;

import br.com.planeta_atlantida.rbs.api.utils.Mensagens;
import org.springframework.beans.factory.annotation.Value;
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

    public ArmazenamentoDocumentoService(@Value("${app.storage.documentos.path}") String diretorio) {
        this.diretorio = Paths.get(diretorio).toAbsolutePath().normalize();
    }


    public String armazenar(MultipartFile arquivo) throws IOException {

        Files.createDirectories(diretorio);

        String chave = UUID.randomUUID().toString();
        Path destino = diretorio.resolve(chave).normalize();

        if (!destino.getParent().equals(diretorio)) {
            throw new IOException(Mensagens.CAMINHO_ARMAZENAMENTO_INVALIDO);
        }

        try (InputStream inputStream = arquivo.getInputStream()) {
            Files.copy(inputStream, destino);
        }

        return chave;

    }

    public void remover(String chave) throws IOException {

        Path arquivo = diretorio.resolve(chave).normalize();

        if (!arquivo.getParent().equals(diretorio)) {
            throw new IOException(Mensagens.CHAVE_ARMAZENAMENTO_INVALIDA);
        }

        Files.deleteIfExists(arquivo);

    }

}

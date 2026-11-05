package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BaixarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.EnviarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import br.com.planeta_atlantida.rbs.api.service.documento.BaixarDocumentoService;
import br.com.planeta_atlantida.rbs.api.service.documento.EnviarDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/patrocinios")
@RequiredArgsConstructor
public class DocumentoController {

    private final EnviarDocumentoService enviarDocumentoService;
    private final BaixarDocumentoService baixarDocumentoService;

    @PostMapping(path = "/{idPatrocinio}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnviarDocumentoResponse> enviar(
                @PathVariable Long idPatrocinio,
                @RequestPart("arquivo") MultipartFile arquivo,
                @RequestParam CategoriaDocumento categoria
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enviarDocumentoService.enviar(idPatrocinio, arquivo, categoria));
    }

    @GetMapping("/{idPatrocinio}/documentos/{idDocumento}/download")
    public ResponseEntity<Resource> baixar(
            @PathVariable("idPatrocinio") Long idpatrocinio,
            @PathVariable("idDocumento") Long idDocumento) {

        BaixarDocumentoResponse documento = baixarDocumentoService.baixar(idDocumento);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(documento.getTipoMime()))
                .contentLength(documento.getTamanhoBytes())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(documento.getNomeArquivo(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                ).body(documento.getArquivo());
    }

}

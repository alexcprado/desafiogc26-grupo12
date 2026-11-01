package br.com.planeta_atlantida.rbs.api.controller;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.EnviarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import br.com.planeta_atlantida.rbs.api.service.documento.EnviarDocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/patrocinios")
@RequiredArgsConstructor
public class DocumentoController {

    private final EnviarDocumentoService enviarDocumentoService;

    @PostMapping(path = "/{idPatrocinio}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnviarDocumentoResponse> enviar(
                @PathVariable Long idPatrocinio,
                @RequestPart("arquivo") MultipartFile arquivo,
                @RequestParam CategoriaDocumento categoria
            ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enviarDocumentoService.enviar(idPatrocinio, arquivo, categoria));
    }

}

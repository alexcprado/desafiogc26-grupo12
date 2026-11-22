package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.EnviarDocumentoResponse;
import br.com.planeta_atlantida.rbs.api.enums.CategoriaDocumento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "Documentos",
        description = "Operações de gerenciamento de documentos"
)
public interface DocumentoControllerDoc {

    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Enviar documento de um patrocínio",
            description = """
                    Realiza o upload de um documento e o vincula
                    ao patrocínio informado.
                    
                    O documento deve ser enviado como multipart/form-data,
                    contendo o arquivo e sua respectiva categoria.
                    
                    **Permissões de acesso:**
                    - **ADMIN:** pode enviar documentos para qualquer patrocínio.
                    - **PATROCINADOR:** pode enviar documentos somente para
                    patrocínios aos quais está vinculado.
                    
                    **Categorias disponíveis:**
                    - FORMULARIO_INGRESSOS
                    - MIV
                    - LOGOTIPO
                    - FOTO_APLICACAO_MARCA
                    - RELATORIO_MIDIA
                    - PLANO_COMERCIAL
                    - CONTRATO
                    - OUTRO
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Documento enviado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados da requisição inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de autenticação ausente ou inválido"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão para enviar documentos ao patrocínio informado"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "O arquivo enviado está vazio",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Não foi possível armazenar o arquivo",
                            content = @Content
                    )
            }
    )
    ResponseEntity<EnviarDocumentoResponse> enviar(

            @Parameter(
                    description = "Identificador do patrocínio ao qual documento será vinculado",
                    example = "1",
                    required = true
            )
            Long idPatrocinio,

            @Parameter(
                    description = "Arquivo que será enviado e armazenado",
                    required = true,
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(
                                    type = "string",
                                    format = "binary"
                            )
                    )
            )
            @RequestPart("arquivo")
            MultipartFile arquivo,

            @Parameter(
                    description = "Categoria do documento",
                    required = true,
                    example = "CONTRATO",
                    schema = @Schema(implementation = CategoriaDocumento.class)
            )
            @RequestParam
            CategoriaDocumento categoria

    );

    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Baixar documento de um patrocínio",
            description = """
                    Realiza o download de um documento vinculado
                    a um patrocínio.
                    
                    O arquivo é retornado com seu tipo MIME original,
                    tamanho em bytes e nome definido no cabeçalho
                    Content-Disposition.
                    
                    **Permissões de acesso:**
                    - **ADMIN:** pode baixar documentos de qualquer patrocínio.
                    - **PATROCINADOR:** pode baixar documentos somente
                    dos patrocínios aos quais está vinculado.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Documento disponibilizado para download",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(
                                            type = "string",
                                            format = "binary"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de autenticação ausente ou inválido"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão para acessar o documento"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Documento ou patrocínio não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    )
            }
    )
    ResponseEntity<Resource> baixar(

            @Parameter(
                    description = "Identificador do patrocínio ao qual o documento pertence",
                    example = "1",
                    required = true
            )
            Long idPatrocinio,

            @Parameter(
                    description = "Identificador único do documento que será baixado",
                    example = "10",
                    required = true
            )
            Long idDocumento

    );

}

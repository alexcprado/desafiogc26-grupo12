package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.response.ListarAuditoriaResponse;
import br.com.planeta_atlantida.rbs.api.doc.annotation.AdminOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Auditorias",
        description = "Operações de gerenciamento de auditorias"
)
public interface AuditoriaControllerDoc {

    @AdminOperation
    @Operation(
            summary = "Listar registros de auditoria",
            description = """
                    Retorna uma lista paginada dos registros de
                    auditoria realizados na plataforma.
                    
                    Permite consultar o histórico de operações
                    registradas pelo sistema, auxiliando no
                    monitoramento e na rastreabilidade das ações.
                    
                    A listagem oferece suporte à paginação por
                    meio dos parâmetros page, size e sort.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registros de auditoria retornados com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros de paginação ou ordenação inválidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    )
            }
    )
    ResponseEntity<Page<ListarAuditoriaResponse>> listar(

            @ParameterObject
            Pageable pageable

    );

}

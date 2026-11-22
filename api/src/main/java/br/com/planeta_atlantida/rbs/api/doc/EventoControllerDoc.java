package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinioRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarPatrocinioResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.BuscarEventoResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.PatrocinioResumoResponse;
import br.com.planeta_atlantida.rbs.api.doc.annotation.AdminOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Eventos",
        description = "Operações de gerenciamento de eventos e patrocínios relacionados"
)
public interface EventoControllerDoc {

    @AdminOperation
    @Operation(
            summary = "Listar eventos",
            description = """
                    Retorna uma lista paginada dos eventos
                    cadastrados na plataforma, com suporte
                    à ordenação dos resultados.
                    
                    Caso não existam eventos cadastrados,
                    retorna uma página vazia.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Eventos listados com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros de paginação ou ordenação inválidos",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Page<BuscarEventoResponse>> listar(

            @ParameterObject
            Pageable pageable

    );

    @AdminOperation
    @Operation(
            summary = "Buscar evento por ID",
            description = """
                    Retorna as informações de um evento cadastrado
                    na plataforma a partir do seu identificador.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Evento encontrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento não encontrado",
                            content = @Content
                    )
            }
    )
    ResponseEntity<BuscarEventoResponse> buscarEvento(

            @Parameter(
                    description = "Identificador único do evento",
                    example = "1",
                    required = true
            )
            Long id

    );

    @AdminOperation
    @Operation(
            summary = "Listar patrocínios de um evento",
            description = """
                    Retorna uma lista paginada dos patrocínios
                    vinculados ao evento informado, com suporte
                    à ordenação dos resultados.
                    
                    Caso não existam patrocínios vinculados ao evento,
                    retorna uma página vazia.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocínios do evento listados com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros de paginação ou ordenação inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento não encontrado",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Page<PatrocinioResumoResponse>> listarPatrocinios(

            @Parameter(
                    description = "Identificador único do evento cujos patrocinios serão listados",
                    example = "1",
                    required = true
            )
            Long id,

            @ParameterObject
            Pageable pageable

    );

    @AdminOperation
    @Operation(
            summary = "Adicionar patrocínio a um evento",
            description = """
                    Cadastra um novo patrocínio, vinculando um
                    patrocinador a um evento específico.
                    
                    Para realizar o cadastro, é necessário informar
                    o identificador do patrocinador e a cota de
                    patrocínio correspondente.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patrocinio cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados da requisição inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evento ou patrocinador não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Patrocinador já possui um patrocínio cadastrado para este evento",
                            content = @Content
                    )
            }
    )
    ResponseEntity<AdicionarPatrocinioResponse> adicionarPatrocinio(

            @Parameter(
                    description = "Identificador único do evento ao qual o patrocínio será vinculado",
                    example = "1",
                    required = true
            )
            Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do patrocinador e da cota de patrocinio",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdicionarPatrocinadorRequest.class),
                            examples = @ExampleObject(
                                    name = "Patrocínio válido",
                                    value = """
                                            {
                                                "idPatrocinador": 1,
                                                "cota": "MASTER"
                                            }
                                            """
                            )
                    )
            )
            AdicionarPatrocinioRequest request
    );

}

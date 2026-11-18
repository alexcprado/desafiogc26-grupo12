package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarCortesiaRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPesquisaAtivacaoRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.*;
import br.com.planeta_atlantida.rbs.api.doc.annotation.AdminOperation;
import br.com.planeta_atlantida.rbs.api.doc.annotation.PatrocinadorOperation;
import br.com.planeta_atlantida.rbs.api.handler.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@Tag(
        name = "Patrocinios",
        description = """
                Operações de gerenciamento de patrocínios e entidades
                relacionadas, como pesquisas de ativação, cortesias
                e relatórios.
                """
)
public interface PatrocinioControllerDoc {

    @AdminOperation
    @Operation(
            summary = "Listar patrocínios",
            description = """
                    Retorna uma lista paginada de patrocínios, com suporte
                    a filtragem por evento e ordenação dos resultados.

                    Caso nenhum evento seja informado, são considerados
                    os patrocínios de todos os eventos.

                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocínios listados com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros de requisição inválidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    ResponseEntity<Page<PatrocinioResumoResponse>> listarTodos(

            @Parameter(
                    description = "Identificador do evento utilizado como filtro",
                    example = "1",
                    required = false
            )
            Long idEvento,

            @ParameterObject
            Pageable pageable

    );

    @PatrocinadorOperation
    @Operation(
            summary = "Listar meus patrocínios",
            description = """
                    Retorna os patrocínios vinculados ao patrocinador
                    associado ao usuário autenticado.
                    
                    caso não existam patrocínios vinculados, retorna
                    uma lista vazia.
                    
                    **Permissão necessária:** PATROCINADOR.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocínios listados com sucesso"
                    )
            }
    )
    ResponseEntity<List<PatrocinioResumoResponse>> listarMeus();

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Buscar todas as informações de um patrocínio específico",
            description = """
                    Retorna as informações detalhadas de um patrocínio
                    a partir do seu identificador.
                    
                    **Permissões necessárias:**
                    
                    - **ADMIN:** pode consultar qualquer patrocínio.
                    - **PATROCINADOR:** pode consultar somente os
                    patrocínios aos quais está vinculado.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Detalhes do patrocinio retornados com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de autenticação ausente ou inválido",
                            content = @Content
                    ),@ApiResponse(
                    responseCode = "403",
                    description = "Usuário não possui permissão para acessar este patrocínio",
                    content = @Content
            ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio não foi encontrado pelo ID",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acesso restrito a ADMIN ou PATROCINADORES vinculados ao patrocínio",
                            content = @Content
                    )
            }
    )
    ResponseEntity<DetalhesPatrocinioResponse> buscarDetalhes(

            @Parameter(
                    description = "Identificador do patrocínio que está sendo buscado",
                    example = "1",
                    required = true
            )
            Long id

    );

    @AdminOperation
    @Operation(
            summary = "Listar pesquisas de ativação",
            description = """
                    Retorna uma lista paginada das pesquisas de ativação
                    cadastradas, com suporte à ordenação dos resultados.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pesquisas de ativação listadas com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros de requisição inválidos",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Page<ListarPesquisasAtivacaoResponse>> listar(
            @ParameterObject
            Pageable pageable
    );

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Buscar pesquisa de ativação",
            description = """
                    Retorn a pesquisa de ativação vinculada ao
                    patrocínio informado.
                    
                    **Permissões necessárias:**
                    
                    - **ADMIN**
                    - **PATROCINADOR**
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pesquisa de ativação retornada com sucesso"
                    ),
                    @ApiResponse(
                    responseCode = "401",
                    description = "Token de autenticação ausente ou inválido",
                    content = @Content
            ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não possui permissão para acessar esta pesquisa",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio ou pesquisa de ativação não encontrada",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<ListarPesquisasAtivacaoResponse> buscarPesquisa(
            @Parameter(
                    description = "Identificador único do patrocínio",
                    example = "1",
                    required = true
            )
            Long id
    );

    ResponseEntity<AdicionarPesquisaAtivacaoResponse> adicionar(

            @Parameter(
                    description = "Identificador do patrocínio ao qual a pesquisa será vinculada",
                    example = "1",
                    required = true
            )
            Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para cadastrar a pesquisa de ativação",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdicionarPesquisaAtivacaoRequest.class),
                            examples = @ExampleObject(
                                    name = "Pesquisa de ativação válida",
                                    value = """
                                            {
                                                "pergunta1": "Como foi a participação da empresa no evento?",
                                                "pergunta2": "Você consideraria comprar produtos da empresa após o evento?",
                                                "pergunta3": "Você recomendaria produtos da empresa para algum amigo ou conhecido?"
                                            }
                                            """
                            )
                    )
            )
            AdicionarPesquisaAtivacaoRequest request
    );

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Listar cortesias de um patrocínio",
            description = """
                Retorna as cortesias vinculadas ao patrocínio
                informado.

                Caso não existam cortesias vinculadas, retorna
                uma lista vazia.

                **Permissões necessárias:**

                - **ADMIN:** pode consultar as cortesias de qualquer
                  patrocínio.
                - **PATROCINADOR:** pode consultar somente as cortesias
                  dos patrocínios aos quais está vinculado.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cortesias listadas com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de autenticação ausente ou inválido",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não possui permissão para acessar as cortesias deste patrocínio",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio não encontrado",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<List<ListarCortesiasResponse>> listar(
            @Parameter(
                    description = "Identificador único do patrocínio",
                    example = "1",
                    required = true
            )
            Long id
    );

    @AdminOperation
    @Operation(
            summary = "Cadastrar cortesia",
            description = """
                Cadastra uma nova cortesia vinculada ao
                patrocínio informado.

                **Permissão necessária:** ADMIN.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cortesia cadastrada com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ou regra de negócio",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio não encontrado",
                            content = @Content
                    )
            }
    )
    ResponseEntity<AdicionarCortesiaResponse> adicionar(

            @Parameter(
                    description = "Identificador do patrocínio ao qual a cortesia será vinculada",
                    example = "1",
                    required = true
            )
            Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para cadastrar uma cortesia",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdicionarCortesiaRequest.class),
                            examples = @ExampleObject(
                                    name = "Cortesia válida",
                                    value = """
                                            {
                                                "setor": "bebidas",
                                                "tipo": "PASSAPORTE",
                                                "quantidade": 10
                                            }
                                            """
                            )
                    )
            )
            AdicionarCortesiaRequest request
    );

    @AdminOperation
    @Operation(
            summary = "Remover cortesia",
            description = """
                Remove uma cortesia vinculada ao patrocínio
                informado.

                **Permissão necessária:** ADMIN.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cortesia removida com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio ou cortesia não encontrada",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Long> remover(

            @Parameter(
                    description = "Identificador único do patrocínio",
                    example = "1",
                    required = true
            )
            Long id,

            @Parameter(
                    description = "Identificador único da cortesia que será removida",
                    example = "1",
                    required = true
            )
            Long idCortesia
    );

    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Gerar relatório de patrocínio",
            description = """
                Gera e retorna um relatório referente ao
                patrocínio informado.

                O relatório é disponibilizado em formato
                de texto simples (text/plain).

                **Permissões necessárias:**

                - **ADMIN:** pode gerar relatórios de qualquer
                  patrocínio.
                - **PATROCINADOR:** pode gerar relatórios somente
                  dos patrocínios aos quais está vinculado.
                """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Relatório gerado com sucesso",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de autenticação ausente ou inválido",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário não possui permissão para gerar o relatório deste patrocínio",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocínio não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno durante a geração do relatório",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    ResponseEntity<String> gerar(

            @Parameter(
                    description = "Identificador do patrocínio cujo relatório será gerado",
                    example = "1",
                    required = true
            )
            Long id

    ) throws IOException;

}
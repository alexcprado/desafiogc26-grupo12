package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AlterarPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.*;
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
        name = "Patrocinadores",
        description = """
                Operações de gerenciamento de patrocinadores,
                como listagem, busca, adição, alteração e desativação
                """
)
public interface PatrocinadorControllerDoc {

    @AdminOperation
    @Operation(
            summary = "Listar patrocinadores",
            description = """
                    Retorna uma lista paginada dos patrocinadores
                    cadastrados na plataforma, com suporte à
                    ordenação dos resultados.
                    
                    Caso não existam patrocinadores cadastrados,
                    retorna uma página vazia.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocinadores listados com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parâmetros de paginação ou ordenação inválidos",
                            content = @Content
                    )
            }
    )
    ResponseEntity<Page<ListarPatrocinadoresResponse>> listar(

            @ParameterObject
            Pageable pageable

    );

    @AdminOperation
    @Operation(
            summary = "Buscar patrocinador por ID",
            description = """
                    Retorna as informações de um patrocinador
                    cadastrado na plataforma a partir do seu
                    identificador.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocinador encontrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocinador não encontrado",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Já existe um patrocinador cadastrado com o CNPJ informado",
                            content = @Content
                    )
            }
    )
    ResponseEntity<PatrocinadorResponse> buscar(

            @Parameter(
                    description = "Identificador único do patrocinador",
                    example = "1",
                    required = true
            )
            Long id

    );

    @AdminOperation
    @Operation(
            summary = "Cadastrar patrocinador",
            description = """
                    Cadastra um novo patrocinador na plataforma,
                    informando seus dados empresariais e de contato.
                    
                    Todos os campos são obrigatórios, e o CNPJ
                    informado deve ser válido e não pode estar
                    cadastrado anteriormente.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Patrocinador cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados da requisição inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Já existe um patrocinador cadastrado com o CNPJ informado",
                            content = @Content
                    )
            }
    )
    ResponseEntity<AdicionarPatrocinadorResponse> adicionar(

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados empresariais e de contato do patrocinador",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AdicionarPatrocinadorRequest.class
                            ),
                            examples = @ExampleObject(
                                    name = "Patrocinador válido",
                                    value = """
                                            {
                                                "nome": "Empresa Exemplo",
                                                "cnpj": "12.345.678/0001-95",
                                                "segmento": "Bebidas",
                                                "contatoPrincipal": "João Silva",
                                                "telefone": "(51) 99999-9999"
                                            }
                                            """
                            )
                    )
            )
            AdicionarPatrocinadorRequest request
    );

    @AdminOperation
    @Operation(
            summary = "Alterar patrocinador",
            description = """
                    Atualiza os dados empresariais e de contato
                    de um patrocinador cadastrado na plataforma,
                    a partir do seu identificador.
                    
                    Todos os campos da requisição são obrigatórios.
                    
                    O CNPJ do patrocinador não pode ser alterado
                    por meio desta operação.
                    
                    **Permissão necessária:** ADMIN.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocinador alterado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados da requisição inválidos",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Patrocinador não encontrado",
                            content = @Content
                    )
            }

    )
    ResponseEntity<AlterarPatrocinadorResponse> alterar(

            @Parameter(
                    description = "Identificador único do patrocinador que será alterado",
                    example = "1",
                    required = true
            )
            Long id,

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados empresariais e de contato que serão atualizados",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = AlterarPatrocinadorRequest.class
                            ),
                            examples = @ExampleObject(
                                    name = "Alteração válida do patrocinador",
                                    value = """
                                            {
                                                "nome": "Empresa Exemplo",
                                                "segmento": "Alimentação e Bebidas",
                                                "contatoPrincipal": "Maria Silva",
                                                "telefone": "(51) 98888-8888"
                                            }
                                            """
                            )
                    )
            )
            AlterarPatrocinadorRequest request
    );

    @AdminOperation
    @Operation(
            summary = "Desativar patrocinador",
            description = """
                    Desativa um patrocinador cadastrado na plataforma
                    a partir do seu identificador.
                    
                    **Permissão necessária:** ADMIN
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrocinador desativado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Patrocinador não encontrado",
                            content = @Content
                    )
            }
    )
    ResponseEntity<DesativarPatrocinadorResponse> desativar(

            @Parameter(
                    description = "Identificador único do patrocinador que será desativado",
                    example = "1",
                    required = true
            )
            Long id

    );

}

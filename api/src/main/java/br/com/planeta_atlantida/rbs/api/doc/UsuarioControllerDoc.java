package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioAdminRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.request.AdicionarUsuarioPatrocinadorRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.AdicionarUsuarioResponse;
import br.com.planeta_atlantida.rbs.api.doc.annotation.AdminOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Usuarios",
        description = "Operações para cadastro de usuários ADMIN e PATROCINADOR"
)
public interface UsuarioControllerDoc {

    @AdminOperation
    @Operation(
            summary = "Cadastrar usuário ADMIN",
            description = """
                    Cria e salva um usuário administrador
                    
                    **Permissão necessária:** ADMIN
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário ADMIN cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ou regra de negócio",
                            content = @Content
                    )
            }
    )
    ResponseEntity<AdicionarUsuarioResponse> adicionarUsuarioAdmin(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para cadastrar um usuário ADMIN",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdicionarUsuarioAdminRequest.class),
                    examples = @ExampleObject(
                            name = "Usuário ADMIN válido",
                            value = """
                                    {
                                        "nome": "João da Silva",
                                        "email": "joao@email.com",
                                        "senha": "12345"
                                    }
                                    """
                    ))
            )
            AdicionarUsuarioAdminRequest request);

    @AdminOperation
    @Operation(
            summary = "Cadastrar usuário PATROCINADOR",
            description = """
                    Cria e salva um usuário vinculado a um patrocinador
                    
                    **Permissão necessária:** ADMIN
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuário PATROCINADOR cadastrado com sucesso"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação ou regra de negócio",
                            content = @Content
                    )
            }
    )
    ResponseEntity<AdicionarUsuarioResponse> adicionarUsuarioPatrocinador(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para cadastrar um usuário PATROCINADOR",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdicionarUsuarioPatrocinadorRequest.class),
                            examples = @ExampleObject(
                                    name = "Usuário PATROCINADOR válido",
                                    value = """
                                    {
                                        "nome": "João da Silva",
                                        "email": "joao@email.com",
                                        "senha": "12345",
                                        "idPatrocinador": 1
                                    }
                                    """
                            ))
            )
            AdicionarUsuarioPatrocinadorRequest request);

}

package br.com.planeta_atlantida.rbs.api.doc;

import br.com.planeta_atlantida.rbs.api.controller.dtos.request.LoginRequest;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.LoginResponse;
import br.com.planeta_atlantida.rbs.api.controller.dtos.response.UsuarioLogadoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Autenticação",
        description = "Operações de login e busca do usuário logado"
)
public interface AuthControllerDoc {

    @Operation(
            summary = "Autenticar usuário",
            description = """
                    Autentica um usuário na plataforma utilizando
                    seu e-mail e senha.
                    
                    Após a validação das credenciais, a API retorna
                    um token JWT que deverá ser utilizado nas
                    requisições aos endpoints protegidos.
                    
                    O token possui validade de **10 minutos (600 segundos)**
                    e contém as permissões do usuário autenticado.
                    
                    **Acesso:** Público. Não é necessário possuir
                    um token JWT para realizar o login.
                    """,
            security = {},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticação realizada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class),
                                    examples = @ExampleObject(
                                            name = "Login realizado",
                                            value = """
                                                    {
                                                        "accessToken": "eyJhbGciOiJSUzI1NiJ9...",
                                                        "expiresIn": 600
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados da requisição inválidos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "E-mail ou senha incorretos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    )
            }
    )
    ResponseEntity<LoginResponse> login(

            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais utilizadas para autenticar o usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(
                                    name = "Credenciais válidas",
                                    value = """
                                            {
                                                 "email": "usuario@exemplo.com",
                                                 "senha": "Senha@123"
                                            }
                                            """
                            )
                    )
            )
            LoginRequest loginRequest
    );

    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Obter usuário autenticado",
            description = """
                    Retorna as informações do usuário autenticado
                    com base no token JWT enviado na requisição.
                    
                    A resposta contém os dados de identificação
                    do usuário, seu perfil de acesso e, quando
                    aplicável, as informações do patrocinador
                    ao qual está vinculado.
                    
                    **Permissões de acesso:**
                    - **ADMIN:** pode consultar suas próprias informações.
                    - **PATROCINADOR:** pode consultar suas próprias informações
                    e os dados do patrocinador ao qual está vinculado.
                    
                    **Autenticação:** requer um token JWT válido.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Informações do usuário autenticado retornadas com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UsuarioLogadoResponse.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Usuário administrador",
                                                    value = """
                                                        {
                                                            "id": 1,
                                                            "nome": "Administrador",
                                                            "email": "admin@exemplo.com",
                                                            "tipo": "ADMIN",
                                                            "idPatrocinador": null,
                                                            "nomePatrocinador": null
                                                        }
                                                        """
                                            ),
                                            @ExampleObject(
                                                    name = "Usuário patrocinador",
                                                    value = """
                                                        {
                                                            "id": 2,
                                                            "nome": "João Silva",
                                                            "email": "joao@exemplo.com",
                                                            "tipo": "PATROCINADOR",
                                                            "idPatrocinador": 10,
                                                            "nomePatrocinador": "Empresa Exemplo"
                                                        }
                                                        """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de autenticação ausente, inválido ou expirado"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Usuário sem permissão para acessar o recurso"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuário autenticado não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema
                            )
                    )
            }
    )
    ResponseEntity<UsuarioLogadoResponse> me();

}

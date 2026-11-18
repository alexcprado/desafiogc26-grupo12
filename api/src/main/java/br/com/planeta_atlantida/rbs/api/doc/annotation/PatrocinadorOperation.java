package br.com.planeta_atlantida.rbs.api.doc.annotation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SecurityRequirement(name = "bearerAuth")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Token de autenticação ausente ou inválido",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Acesso restrito a usuários com permissão PATROCINADOR",
                content = @Content
        )
})
public @interface PatrocinadorOperation {
}

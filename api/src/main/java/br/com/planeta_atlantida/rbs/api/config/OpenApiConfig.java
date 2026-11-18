package br.com.planeta_atlantida.rbs.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Planeta Atlântida API")
                        .description(
                                """
                                API REST desenvolvida para centralizar e facilitar o
                                gerenciamento de patrocinadores e patrocínios do
                                Planeta Atlântida.
                                        
                                A aplicação permite gerenciar as informações relacionadas
                                às diferentes edições do evento, acompanhar os patrocínios
                                e disponibilizar informações estratégicas aos patrocinadores.
                                        
                                ## Principais funcionalidades
                                        
                                - Gerenciamento de usuários e permissões de acesso.
                                - Gerenciamento de eventos e suas respectivas edições.
                                - Gerenciamento de patrocinadores.
                                - Gerenciamento de patrocínios.
                                - Gerenciamento de cortesias vinculadas aos patrocínios.
                                - Gerenciamento de pesquisas de ativação.
                                - Geração de relatórios de patrocínio.
                                        
                                ## Autenticação e autorização
                                        
                                A API utiliza autenticação baseada em JWT e controle de
                                acesso baseado em papéis (RBAC), com dois perfis de usuário:
                                
                                - **ADMIN:** responsável pelo gerenciamento administrativo
                                da plataforma.
                                - **PATROCINADOR:** possui acesso restrito às informações
                                e funcionalidades dos patrocínios aos quais está vinculado.
                                        
                                ## Sobre o projeto
                                        
                                Projeto desenvolvido durante o programa Geração Caldeira 2026,
                                em atendimento ao desafio proposto para o Planeta Atlântida.
                                """
                        ).version("v1.0.0")
                        .contact(new Contact()
                                .name("Geração Caldeira")
                                .email("geracaoCaldeira@email.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                )
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor Local")))
                .externalDocs(new ExternalDocumentation().description("Documentação do projeto")
                        .url("https://github.com/alexcprado/desafiogc26-grupo12"))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtido através da autenticação na API")
                        ));
    }

}

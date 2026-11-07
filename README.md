# Planeta Atlântida — API de gestão de patrocínios

API REST do grupo 12 para apoiar a gestão dos patrocinadores do Planeta Atlântida. A aplicação organiza eventos por ano, cadastra patrocinadores e usuários, registra cotas de patrocínio, recebe documentos de comprovação e mantém pesquisas de ativação e um histórico de auditoria.

## Índice

- [Visão geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Stack tecnológica](#stack-tecnológica)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Modelo de dados](#modelo-de-dados)
- [Configuração do ambiente](#configuração-do-ambiente)
- [Executando o projeto](#executando-o-projeto)
- [Autenticação](#autenticação)
- [Endpoints da API](#endpoints-da-api)
- [Regras de negócio](#regras-de-negócio)
- [Erros e validações](#erros-e-validações)
- [Testes](#testes)

## Visão geral

O sistema oferece uma API para administrar o ciclo de patrocínio de eventos:

```text
Evento anual
    ↓
Cadastro de patrocinadores
    ↓
Vínculo do patrocinador ao evento com uma cota
    ↓
Envio de documentos associados ao patrocínio
    ↓
Pesquisa de ativação (quando a cota permite)
    ↓
Registro das ações para auditoria
```

Existem dois perfis de usuário: **ADMIN**, que administra eventos, patrocinadores e usuários, e **PATROCINADOR**, que pode consultar o próprio patrocínio, enviar documentos e, quando elegível, registrar uma pesquisa de ativação.

O repositório contém o backend da API em `api/` e o front-end React + Vite em `web/`.

## Funcionalidades

- Login por e-mail e senha, com senha armazenada usando BCrypt e emissão de JWT assinado com RSA.
- Cadastro e consulta de eventos.
- Cadastro, listagem paginada, edição e desativação de patrocinadores.
- Criação de usuários administradores e de usuários vinculados a um patrocinador.
- Associação de patrocinadores a eventos com cotas `MASTER`, `SUBCOTA_G` ou `SUBCOTA`.
- Consulta dos detalhes de um patrocínio e dos documentos associados.
- Upload e download de documentos por categoria, armazenados no sistema de arquivos.
- Registro de uma pesquisa de ativação por patrocínio, restrita a cotas `MASTER`.
- Gestão de cortesias vinculadas ao patrocínio, com setor, tipo e quantidade.
- Geração de relatório consolidado do patrocínio com auxílio de IA (Google Gemini), extraindo textos de documentos PDF/DOCX e sintetizando os dados em linguagem profissional em português.
- Auditoria de logins e de operações relevantes, como alteração de patrocinador, vínculo de patrocínio, movimentação de documentos, pesquisa de ativação e cortesias.
- Endpoints de saúde e monitoramento do Spring Boot Actuator.

### Novas funcionalidades adicionadas

#### 1) Gestão de cortesias por patrocínio

A API permite que o administrador registre cortesias vinculadas a um patrocínio, definindo:

- `setor`: área de atuação da cortesia (`MUSICA`, `ESPORTE`, `FESTIVAL`, etc., conforme enum do domínio).
- `tipo`: tipo de cortesia (`SEXTA`, `SABADO`, `PASSAPORTE`).
- `quantidade`: número de cortesias ofertadas.

A funcionalidade cobre:

- cadastro de uma cortesia para um patrocínio;
- listagem das cortesias do patrocínio;
- remoção da cortesia cadastrada;
- registro em auditoria de cada operação.

A regra de negócio impede que o mesmo patrocínio tenha mais de uma cortesia cadastrada ao mesmo tempo, preservando integridade do conjunto de benefícios do patrocínio.

#### 2) Pesquisa de ativação do patrocínio

A pesquisa de ativação é um questionário obrigatório para patrocínios de cota `MASTER`, com três perguntas:

- `pergunta1`
- `pergunta2`
- `pergunta3`

A API valida:

- que as três perguntas tenham conteúdo útil;
- que só seja permitida para patrocínios de cota `MASTER`;
- que cada patrocínio só possa ter uma única pesquisa cadastrada;
- que o usuário autenticado tenha permissão de acessar o patrocínio em questão.

Essa funcionalidade é relevante para captar dados do nível de ativação do patrocinador e reunir um histórico das respostas no contexto do patrocínio.

#### 3) Relatório inteligente com Gemini

A aplicação inclui a rota `GET /patrocinios/{id}/relatorio`, que gera um relatório textual do patrocínio em português brasileiro com base nos documentos enviados para esse patrocínio.

O fluxo implementado:

- busca os documentos do patrocínio em ordem de criação;
- filtra somente categorias relevantes para análise (
  `RELATORIO_MIDIA`, `FORMULARIO_INGRESSOS`, `PLANO_COMERCIAL` e `CONTRATO`);
- extrai texto de arquivos PDF, DOCX e TXT;
- limita a quantidade de conteúdo processado para manter a operação eficiente;
- envia o contexto ao modelo de IA do Google Gemini;
- retorna um relatório estruturado com resumo executivo, objetivos, entregas, métricas, indicadores, conclusões e pendências.

A geração do relatório respeita regras de segurança e consistência:

- nunca inventa informações;
- usa apenas o conteúdo dos documentos fornecidos;
- diferencia metas planejadas e resultados executados;
- informa a origem dos dados e a fórmula de indicadores quando calculados;
- declara ausência de informação quando os documentos não contemplam a métrica;
- ignora instruções internas que possam aparecer nos materiais analisados.

Esse recurso transforma a API em uma base operacional para análise e consolidação de entregas de patrocínio sem exigir intervenção manual para montar um resumo executivo.

#### 4) Fluxo completo de documentação e validação

Além do upload e da operação de download, o sistema incorpora regras mais robustas para a combinação de documentos e informações do patrocínio:

- cada patrocínio aceita apenas um documento por categoria;
- o upload verifica arquivo vazio, tamanho e categoria válida;
- a aplicação salva o arquivo no diretório configurado e registra o metadado no banco;
- o sistema valida que o usuário autenticado tenha acesso ao patrocínio antes de enviar ou baixar qualquer documento;
- o arquivo é lido com segurança para extração de conteúdo textual quando a IA gera o relatório.

Essas medidas reduzem inconsistências na base documental e deixam o ciclo de comprovação do patrocínio mais controlado.

## Arquitetura

A aplicação segue uma arquitetura em camadas:

```text
HTTP / DTOs → controllers → services → repositories → PostgreSQL
                   ↓            ↓
                mappers      validators
                                ↓
                           domínio / regras
```

- **controller**: define as rotas REST, recebe e valida DTOs e monta respostas HTTP.
- **service**: coordena as operações, as regras de negócio, as transações e a auditoria.
- **repository**: persistência com Spring Data JPA.
- **domain**: entidades JPA que representam o modelo de negócio.
- **mapper**: conversão entre entidades e DTOs.
- **validator**: validações de existência, permissão e regras específicas de patrocínio e documentos.
- **config**: configuração de segurança JWT e CORS.
- **exception / handler**: exceções de domínio e resposta global para erros de validação.

## Stack tecnológica

| Componente | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| API web | Spring MVC |
| Segurança | Spring Security, OAuth2 Resource Server e JWT RSA |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | PostgreSQL 14 |
| Validação | Jakarta Validation |
| Build | Maven (Maven Wrapper incluído) |
| Containerização | Docker e Docker Compose |
| Monitoramento | Spring Boot Actuator |

## Estrutura do projeto

```text
.
├── README.md
├── api/                           # API Spring Boot
│   ├── data/
│   │   ├── docker-compose.yml       # PostgreSQL e API em containers
│   │   ├── README.MD               # Instruções básicas do Compose
│   │   └── scripts/
│   │       ├── 1-schema.sql        # Tabelas e restrições do banco
│   │       └── 2-Insert-mock.sql   # Evento e usuário inicial de exemplo
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/br/com/planeta_atlantida/rbs/api/
│   │   │   │   ├── config/         # Segurança e CORS
│   │   │   │   ├── controller/     # Controllers REST e DTOs
│   │   │   │   ├── domain/         # Entidades JPA
│   │   │   │   ├── enums/          # Perfis, cotas, categorias e auditoria
│   │   │   │   ├── exception/      # Exceções da aplicação
│   │   │   │   ├── handler/        # Tratamento de erros
│   │   │   │   ├── mapper/         # Conversão entre domínio e DTOs
│   │   │   │   ├── repository/     # Repositórios JPA
│   │   │   │   ├── service/        # Lógica de negócio por funcionalidade
│   │   │   │   ├── utils/          # Mensagens e utilitários
│   │   │   │   └── validator/      # Regras e verificações
│   │   │   └── resources/
│   │   │       ├── application.yml # Configuração da aplicação
│   │   │       ├── authz.pub       # Chave pública para JWT
│   │   │       └── authz.pem       # Chave privada para JWT
│   │   └── test/                   # Teste de inicialização do contexto
│   ├── storage/documentos/         # Diretório padrão de documentos enviados
│   ├── Dockerfile
│   ├── mvnw / mvnw.cmd
│   └── pom.xml
└── web/                           # Front-end React + Vite
    ├── src/
    │   ├── app/                   # Rotas e providers
    │   ├── pages/                 # Composição por rota
    │   ├── entities/              # Uma pasta por entidade do back-end
    │   └── shared/                # API client, sessão, UI e layout
    ├── public/                    # Logo do evento
    └── README.md                  # Como rodar e decisões do front
```

## Modelo de dados

As tabelas centrais e seus relacionamentos são:

```text
evento (ano)
  └── patrocinio ── patrocinador
        ├── documento ── usuario (remetente)
        └── pesquisa_ativacao

usuario ── patrocinador (opcional; obrigatório para PATROCINADOR)
auditoria ── usuario (opcional, preserva dados do usuário na própria auditoria)
```

- **evento**: identifica uma edição anual do evento.
- **patrocinador**: empresa participante, identificada por CNPJ e com status `ATIVO` ou `INATIVO`.
- **patrocinio**: vínculo entre evento e patrocinador, incluindo a cota contratada.
- **usuario**: conta de acesso do tipo `ADMIN` ou `PATROCINADOR`.
- **documento**: metadados do arquivo, categoria, remetente e referência ao patrocínio. O conteúdo é gravado em disco, não no PostgreSQL.
- **pesquisa_ativacao**: até uma pesquisa por patrocínio, com três perguntas.
- **auditoria**: ação, entidade, usuário e data/hora da operação.

O script de schema define restrições de unicidade, como CNPJ, e-mail, vínculo evento/patrocinador, uma categoria de documento por patrocínio e uma pesquisa por patrocínio. O Hibernate está configurado com `ddl-auto: update`; o script SQL continua sendo usado pelo Compose na inicialização de um volume novo.

## Configuração do ambiente

### Pré-requisitos

- Java 21 (JDK), para executar localmente.
- Docker com Docker Compose, para subir o PostgreSQL ou toda a aplicação.

### Configuração padrão

O `application.yml` usa os seguintes valores por padrão:

| Configuração | Padrão | Variável de ambiente |
| --- | --- | --- |
| URL JDBC | `jdbc:postgresql://localhost:5432/db-planeta_atlantida` | `DB_URL` |
| Usuário do banco | `postgres` | Não parametrizado no `application.yml` |
| Senha do banco | `postgres` | Não parametrizado no `application.yml` |
| Porta HTTP | `8080` | Configurada no `application.yml` |
| Diretório de documentos | `./storage/documentos` | `DOCUMENTOS_PATH` |

O Compose configura `DB_URL` para que o container da API encontre o container PostgreSQL. Para outro ambiente, ajuste a configuração da aplicação e forneça credenciais adequadas; os valores padrão são destinados ao desenvolvimento local.

## Executando o projeto

### API e banco em containers

Na pasta `api\data`, execute:

```bash
docker compose up --build -d
```

O Compose inicia:

- PostgreSQL 14 na porta `5432`, com banco `db-planeta_atlantida`.
- API na porta `8080`, após o banco passar no health check.
- Volumes persistentes para banco e documentos.

Os scripts em `api/data/scripts/` são executados automaticamente pelo PostgreSQL quando o volume de dados é criado pela primeira vez. Para parar os serviços:

```bash
docker compose down
```

Para apagar também os dados persistidos e recriar o banco do zero:

```bash
docker compose down -v
```

> `down -v` remove os volumes do Compose, incluindo os dados locais do banco e os documentos persistidos.

### API local com PostgreSQL em container

1. Na pasta `api\data`, inicie apenas o banco:

   ```bash
   docker compose up -d postgres
   ```

2. Na pasta `api`, execute a aplicação:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

A aplicação ficará disponível em `http://localhost:8080`.

### Build e execução dos testes

Na pasta `api`:

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd test
```

### Front-end

O front fica em `web/` e consome a API. Com a API rodando em `http://localhost:8080` (porta padrão do `application.yml`):

```bash
cd web
npm install
npm run dev
```

O Vite sobe em `http://localhost:4200` e encaminha `/api` para `http://localhost:8080`. Se a API estiver em outra porta, aponte o proxy:

```bash
VITE_PROXY_TARGET=http://localhost:8081 npm run dev
```

`npm run preview` usa o mesmo proxy. Para servir o `dist` em um servidor sem proxy, gere o build com a URL completa da API:

```bash
VITE_API_URL=http://localhost:8080 npm run build
```

Detalhes de estrutura e decisões em `web/README.md`.

### Verificação rápida

Com a API e o front rodando, o script da raiz confere os quatro pontos que costumam falhar (API no ar, login direto, login pelo proxy e index do front):

```bash
./verificar.sh
# outra porta: API_URL=http://localhost:8081 WEB_URL=http://localhost:4173 ./verificar.sh
```

## Autenticação

O endpoint de login é público. Nas demais rotas de negócio, envie o JWT retornado no cabeçalho HTTP `Authorization`, usando o esquema `Bearer`.

### Acesso inicial em desenvolvimento

O script de mock cria apenas o usuário do grupo, com um hash BCrypt cuja senha não está documentada — e não existe
cadastro público nem recuperação de senha. Para um banco novo não ficar inacessível, a API cria uma conta ADMIN de
desenvolvimento na primeira execução, se ela ainda não existir:

```text
e-mail: admin@planeta.dev
senha:  admin123
```

As variáveis `SEED_ADMIN_EMAIL` e `SEED_ADMIN_SENHA` mudam essas credenciais, e `SEED_ADMIN=false` desliga a criação.
Use apenas em desenvolvimento. Com essa conta é possível criar outras em `POST /usuarios/admins` ou pela tela
**Equipe RBS** do front.

Faça login enviando e-mail e senha:

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "email": "usuario@exemplo.com",
  "senha": "sua-senha"
}
```

A resposta contém `accessToken` e `expiresIn` (600 segundos). O token inclui o e-mail como sujeito e o perfil no claim `roles`. As senhas de usuários são verificadas com BCrypt.

O script de dados de exemplo cria um usuário ADMIN e o evento de 2026. A senha está armazenada como hash no script; use uma conta e senha conhecidas do ambiente local ou crie/configure o usuário apropriado antes de testar as rotas autenticadas.

### Acesso por perfil

- **ADMIN**: acesso administrativo a usuários, eventos, patrocinadores e auditoria; também pode operar sobre qualquer patrocínio.
- **PATROCINADOR**: acesso às operações de patrocínio permitido ao perfil, limitado ao patrocínio vinculado ao próprio patrocinador.
- Rotas `/usuarios/**`, `/patrocinadores/**`, `/eventos/**` e `/auditorias/**` exigem perfil ADMIN.
- Rotas de `/patrocinios/**` exigem autenticação; as operações de detalhe, pesquisa e documentos validam também o vínculo do usuário patrocinador com o patrocínio.
- Os endpoints GET expostos pelo Actuator (`/actuator/**`) são públicos.

## Endpoints da API

Todas as rotas abaixo são relativas à raiz, por exemplo `http://localhost:8080`. Salvo indicação em contrário, envie e receba JSON. Rotas paginadas aceitam os parâmetros padrão do Spring Data, como `page` e `size`.

### Autenticação

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/auth/login` | Público | Autentica o usuário e retorna JWT. |

### Eventos e patrocínios

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/eventos/{id}` | ADMIN | Consulta um evento pelo identificador. |
| `POST` | `/eventos/{id}/patrocinadores` | ADMIN | Cadastra um patrocinador. |
| `POST` | `/eventos/{id}/patrocinios` | ADMIN | Vincula um patrocinador existente ao evento, com uma cota. |
| `GET` | `/patrocinios/{id}/detalhes` | ADMIN ou patrocinador vinculado | Consulta dados do patrocínio e documentos associados. |

O corpo de `POST /eventos/{id}/patrocinadores` usa os campos `nome`, `cnpj`, `segmento`, `contatoPrincipal` e `telefone`. **Observação sobre a implementação atual:** embora a rota contenha `{id}`, esse identificador de evento não é usado pelo serviço de cadastro do patrocinador; o cadastro não cria o vínculo com o evento. Para criar o vínculo, use `POST /eventos/{id}/patrocinios`.

Exemplo de corpo para criar um vínculo:

```json
{
  "idPatrocinador": 1,
  "cota": "MASTER"
}
```

Valores aceitos para `cota`: `MASTER`, `SUBCOTA_G` e `SUBCOTA`.

### Patrocinadores

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/patrocinadores` | ADMIN | Lista patrocinadores com paginação. |
| `POST` | `/patrocinadores` | ADMIN | Cadastra um patrocinador. |
| `PUT` | `/patrocinadores/{id}` | ADMIN | Altera nome, segmento, contato principal e telefone. |
| `DELETE` | `/patrocinadores/{id}` | ADMIN | Desativa o patrocinador (não o remove fisicamente). |

O cadastro exige `nome`, `cnpj`, `segmento`, `contatoPrincipal` e `telefone`; o CNPJ é validado. Na alteração, o CNPJ não é alterado.

### Usuários

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/usuarios/admins` | ADMIN | Cria uma conta ADMIN. |
| `POST` | `/usuarios/patrocinadores` | ADMIN | Cria uma conta vinculada a um patrocinador. |

Para criar um ADMIN, envie `nome`, `email` e `senha`. Para criar um usuário patrocinador, envie também `idPatrocinador`. As senhas são armazenadas como hash BCrypt.

### Documentos

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `POST` | `/patrocinios/{idPatrocinio}/documentos` | ADMIN ou patrocinador vinculado | Envia um documento associado ao patrocínio. |
| `GET` | `/patrocinios/{idPatrocinio}/documentos/{idDocumento}/download` | ADMIN ou patrocinador vinculado | Baixa o arquivo enviado. |

O upload usa `multipart/form-data` com:

- `arquivo`: arquivo a enviar.
- `categoria`: uma das categorias abaixo.

Categorias aceitas: `FORMULARIO_INGRESSOS`, `MIV`, `LOGOTIPO`, `FOTO_APLICACAO_MARCA`, `RELATORIO_MIDIA`, `PLANO_COMERCIAL`, `CONTRATO` e `OUTRO`. O limite configurado para upload é de 10 MB por arquivo e por requisição. Cada patrocínio pode ter no máximo um documento de cada categoria.

### Pesquisas de ativação

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/patrocinios/pesquisa-ativacao` | ADMIN | Lista pesquisas com paginação. |
| `POST` | `/patrocinios/{id}/pesquisa-ativacao` | ADMIN ou patrocinador vinculado | Registra pesquisa para o patrocínio. |

O corpo do POST deve conter três perguntas não vazias: `pergunta1`, `pergunta2` e `pergunta3`. A pesquisa só pode ser criada uma vez e somente para patrocínios de cota `MASTER`.

### Cortesias

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/patrocinios/{id}/cortesias` | ADMIN ou patrocinador vinculado | Lista as cortesias vinculadas ao patrocínio. |
| `POST` | `/patrocinios/{id}/cortesias` | ADMIN | Cadastra uma cortesia para o patrocínio. |
| `DELETE` | `/patrocinios/{id}/cortesias/{idCortesia}` | ADMIN | Remove a cortesia cadastrada. |

O cadastro exige `setor`, `tipo` e `quantidade`. Os valores aceitos para `tipo` são `SEXTA`, `SABADO` e `PASSAPORTE`. O conjunto de cortesias é tratado como um cadastro único por patrocínio, e cada operação é registrada na auditoria.

### Relatório do patrocínio

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/patrocinios/{id}/relatorio` | ADMIN ou patrocinador vinculado | Gera um relatório textual do patrocínio com base nos documentos enviados. |

A rota produz um texto em português brasileiro estruturado em blocos de análise do patrocínio. Ela considera documentos relevantes da categoria de mídia/comercial e utiliza a integração com Gemini para transformar o material em um resumo executivo e diagnóstico operacional.

### Auditoria

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/auditorias` | ADMIN | Lista registros de auditoria com paginação. |

O sistema registra, entre outras operações, login, cadastro/alteração/desativação de patrocinadores, associação de patrocínio, criação de pesquisa e envio/download de documentos.

### Monitoramento

| Método | Rota | Acesso | Descrição |
| --- | --- | --- | --- |
| `GET` | `/actuator/health` | Público | Estado de saúde da aplicação. |
| `GET` | `/actuator/info` | Público | Informações da aplicação, quando disponíveis. |
| `GET` | `/actuator/prometheus` | Público | Métricas no formato Prometheus. |

O Actuator está configurado para expor `health`, `info` e `prometheus`.

## Regras de negócio

- Cada evento representa um ano único, limitado pelo schema entre 2000 e 2100.
- Um patrocinador só pode ser vinculado uma vez ao mesmo evento.
- Um usuário `PATROCINADOR` deve estar associado a um patrocinador; um `ADMIN` não possui esse vínculo.
- Usuários patrocinadores só podem acessar patrocínios do patrocinador ao qual pertencem.
- A desativação de patrocinador altera seu status para `INATIVO`, preservando o registro.
- Cada patrocínio pode ter uma pesquisa de ativação, permitida apenas para a cota `MASTER`.
- Cada patrocínio pode ter uma única cortesia cadastrada por conjunto de dados; a regra impede duplicações fora do modelo esperado.
- Cada categoria de documento pode aparecer no máximo uma vez para o mesmo patrocínio.
- Os arquivos são armazenados em `DOCUMENTOS_PATH` (por padrão, `./storage/documentos`); o banco guarda seus metadados e uma chave de armazenamento gerada pela aplicação.
- O relatório do patrocínio usa apenas documentos relevantes e extrai texto por meio de parsers de PDF, DOCX e TXT antes de enviar o contexto para a IA.
- Tokens JWT expiram após 600 segundos.

## Erros e validações

Os DTOs aplicam validações de campos obrigatórios, e-mail, tamanho e CNPJ. Erros de validação de entrada são respondidos com HTTP 400. As exceções de domínio declaram status HTTP, incluindo:

| Status | Exemplos |
| --- | --- |
| `400 Bad Request` | Arquivo vazio, categoria ausente ou campos inválidos. |
| `401 Unauthorized` | Credenciais inválidas ou operação sem permissão. |
| `404 Not Found` | Evento, patrocínio, patrocinador ou documento inexistente. |
| `409 Conflict` | CNPJ duplicado, patrocinador já vinculado, categoria de documento repetida ou pesquisa já existente. |
| `500 Internal Server Error` | Falha ao armazenar arquivo. |

O formato de erro de validação inclui timestamp, status, mensagem e, quando aplicável, um mapa de erros por campo.

## Testes

O diretório `api/src/test` contém um teste de integração de inicialização do contexto Spring (`ApiApplicationTests`). Para executá-lo, use na pasta `api`:

```powershell
.\mvnw.cmd test
```

Também é possível compilar e empacotar a API com:

```powershell
.\mvnw.cmd clean package
```

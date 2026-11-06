# Portal do Patrocinador — Planeta Atlântida

Front-end React + Vite do desafio GC26 (grupo 12). Consome a API Spring Boot em `../api` e cobre os dois
perfis do sistema: **ADMIN** (equipe RBS) e **PATROCINADOR** (marca).

## Rodando

```bash
npm install
npm run dev
```

- Dev server: `http://localhost:4200`
- O `/api` é proxiado para `http://localhost:8081` (veja `vite.config.ts`). Suba a API nessa porta:

```bash
cd ../api
DB_URL=jdbc:postgresql://localhost:5432/db-planeta_atlantida \
DB_USERNAME=postgres DB_PASSWORD=postgres \
SERVER_PORT=8081 sh ./mvnw spring-boot:run
```

Para apontar para outra API, defina `VITE_API_URL` no `.env` (ex.: `VITE_API_URL=http://localhost:8080`).

```bash
npm run build     # gera dist/
npm run typecheck # tsc --noEmit
```

## Estrutura por entidade

Cada entidade concentra o que precisa para funcionar: os tipos que recebe do back-end, as chamadas de API e
os componentes de tela. Páginas apenas compõem entidades e cuidam de rota.

```text
src/
├── app/                     # App, rotas
├── pages/                   # composição por rota (login, painel, patrocinadores, ...)
├── entities/
│   ├── auth/                # types, api, LoginForm
│   ├── usuario/             # criação de contas ADMIN e PATROCINADOR
│   ├── evento/              # edições anuais
│   ├── patrocinador/        # marca, formulário, badge de status
│   ├── patrocinio/          # vínculo marca↔evento, painel com abas
│   ├── documento/           # upload/download por categoria
│   ├── cortesia/            # setores, tipos e quantidades
│   ├── pesquisa-ativacao/   # três perguntas da cota Master
│   └── auditoria/           # histórico de ações
└── shared/
    ├── api/                 # client fetch, tokens, tipos de página, enums
    ├── auth/                # sessão (JWT), guarda de rota
    ├── layout/              # shell com sidebar e tema
    ├── lib/                 # formatação, cn, hook de dados
    └── ui/                  # primitivas (botão, input, card, tabela, abas, modal, toast)
```

## Autenticação e sessão

- `POST /auth/login` devolve `accessToken` (JWT RSA, 600 s) e `expiresIn`.
- O token fica em `localStorage` (`planeta.token`) e é enviado como `Authorization: Bearer`.
- A sessão lê `exp` do próprio JWT e agenda o encerramento; qualquer resposta `401` também encerra.
- Ao expirar, o usuário volta para `/login?expirado=1` com aviso na tela.
- Rotas são protegidas por perfil: `/painel`, `/patrocinadores`, `/patrocinios`, `/auditoria` e `/usuarios`
  exigem ADMIN; `/minha-marca` exige PATROCINADOR.

## Design

Tokens em `src/index.css` seguem o design do Lovable (`portalplaneta.lovable.app`): tema escuro por padrão,
paleta oklch, tipografia Figtree + Bricolage Grotesque, raio de 8 px e componentes shadcn-like. Há alternância
claro/escuro no rodapé da sidebar e na tela de login.

## Endpoints adicionados na API

O front precisava de leituras que o back-end original não expunha. Foram adicionadas seguindo o mesmo padrão
de camadas (`controller → service → repository → mapper`):

| Rota | Acesso | Para quê |
| --- | --- | --- |
| `GET /auth/me` | autenticado | dados do usuário logado e vínculo com patrocinador |
| `GET /eventos` | ADMIN | seletor de edição |
| `GET /eventos/{id}/patrocinios` | ADMIN | marcas do evento |
| `GET /patrocinadores/{id}` | ADMIN | detalhe da marca |
| `GET /patrocinios` | ADMIN | lista paginada com filtro `idEvento` |
| `GET /patrocinios/meus` | PATROCINADOR | patrocínios da própria marca |
| `GET /patrocinios/{id}/pesquisa-ativacao` | ADMIN/PATROCINADOR | pesquisa já registrada |

Também foram corrigidos: `DB_USERNAME`/`DB_PASSWORD` no `application.yml` (o Compose já os passava), o status
do corpo de erro de validação (dizia 404 em resposta 400), o handler das exceções de domínio (respeita o
`@ResponseStatus`) e o relatório de patrocínio (fallback local quando o provedor de IA está indisponível).

# Docker Compose - PostgreSQL 14

Este arquivo explica como executar o docker-compose e o que cada configuração faz.

---

# ▶️ Como executar o projeto

### 1️⃣ Subir os containers

Dentro da pasta /data, rodar o seguinte comando por terminal:
```bash
docker compose up -d
```

O parâmetro `-d` executa em segundo plano.

---

### 2️⃣ Parar os containers

```bash
docker compose down
```

---

### 3️⃣ Parar e remover volumes (apaga o banco de dados)

```bash
docker compose down -v
```

Obs: use com cuidado, caso o banco de dados seja alterado e precisa recriar as tabelas, rode esse comando para apagar o
banco atual.  
E depois suba os containers novamente com `docker compose up -d` para criar um banco limpo.

---

# 📌 Resumo

- PostgreSQL 14 rodando em container
- Dados persistidos em volume
- Fácil inicialização e remoção

Pronto para usar em projetos Node, Java, Spring ou qualquer aplicação que utilize PostgreSQL.

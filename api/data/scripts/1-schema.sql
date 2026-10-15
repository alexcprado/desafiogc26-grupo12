DROP TABLE IF EXISTS notificacao CASCADE;
DROP TABLE IF EXISTS tarefa CASCADE;
DROP TABLE IF EXISTS patrocinio CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS patrocinador CASCADE;
DROP TABLE IF EXISTS evento CASCADE;

CREATE TABLE evento (
    id SERIAL PRIMARY KEY NOT NULL,
    ano SMALLINT NOT NULL UNIQUE CHECK (ano BETWEEN 2000 AND 2100)
);

CREATE TABLE patrocinador (
    id SERIAL PRIMARY KEY NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    razao_social VARCHAR(255) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'ATIVO' CHECK (status IN ('ATIVO', 'INATIVO')),
    data_hora_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    data_hora_alteracao TIMESTAMPTZ
);


CREATE TABLE patrocinio (
    id SERIAL PRIMARY KEY,
    id_evento INT NOT NULL REFERENCES evento (id),
    id_patrocinador INT NOT NULL REFERENCES patrocinador (id),
    cota VARCHAR(50) NOT NULL,
    valor_contratado NUMERIC(12, 2) NOT NULL CHECK (valor_contratado >= 0),
    CONSTRAINT uq_patrocinio_evento_patrocinador UNIQUE (id_evento, id_patrocinador)
);


CREATE TABLE usuario (
    id SERIAL PRIMARY KEY NOT NULL,
    id_patrocinador INT REFERENCES patrocinador (id),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha_hash VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ADMIN', 'PATROCINADOR')),
    data_hora_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    data_hora_alteracao TIMESTAMPTZ,
    CONSTRAINT ck_usuario_vinculo_patrocinador
    CHECK ((tipo = 'ADMIN' AND id_patrocinador IS NULL)
    OR (tipo = 'PATROCINADOR' AND id_patrocinador IS NOT NULL))
);

CREATE TABLE tarefa (
    id SERIAL PRIMARY KEY NOT NULL,
    id_patrocinio INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    prazo DATE NOT NULL,
    data_hora_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    data_hora_alteracao TIMESTAMPTZ,
    CONSTRAINT ck_status CHECK (status in ('PENDENTE', 'ENTREGUE', 'ATRASADO')),
    CONSTRAINT fk_tarefa_patrocinio FOREIGN KEY (id_patrocinio) REFERENCES patrocinio (id)
);

CREATE TABLE notificacao (
    id SERIAL PRIMARY KEY NOT NULL,
    id_patrocinador INT NOT NULL,
    id_usuario_administrador INT NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    conteudo TEXT NOT NULL,
    data_hora_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_notificacao_patrocinador FOREIGN KEY (id_patrocinador) REFERENCES patrocinador (id),
    CONSTRAINT fk_notificacao_usuario_administrador FOREIGN KEY (id_usuario_administrador) REFERENCES usuario (id)
);
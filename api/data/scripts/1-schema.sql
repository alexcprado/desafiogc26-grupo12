DROP TABLE IF EXISTS documento CASCADE;
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
    id SERIAL PRIMARY KEY NOT NULL,
    id_evento INT NOT NULL REFERENCES evento (id),
    id_patrocinador INT NOT NULL REFERENCES patrocinador (id),
    cota VARCHAR(30) NOT NULL CHECK (cota IN ('MASTER', 'SUBCOTA_G', 'SUBCOTA')),
    valor_contratado NUMERIC(12, 2) NOT NULL CHECK (valor_contratado >= 0),
    data_hora_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    data_hora_alteracao TIMESTAMPTZ,
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

CREATE TABLE documento (
    id SERIAL PRIMARY KEY NOT NULL,
    id_patrocinio INT NOT NULL REFERENCES patrocinio (id),
    id_usuario_envio INT NOT NULL REFERENCES usuario (id),
    nome_arquivo VARCHAR(255) NOT NULL,
    categoria VARCHAR(30) NOT NULL
    CHECK (categoria IN (
            'FORMULARIO_INGRESSOS',
            'MIV',
            'LOGOTIPO',
            'FOTO_APLICACAO_MARCA',
            'RELATORIO_MIDIA',
            'CONTRATO',
            'OUTRO'
        )),
    chave_armazenamento VARCHAR(500) NOT NULL UNIQUE,
    tipo_mime VARCHAR(100) NOT NULL,
    tamanho_bytes BIGINT NOT NULL CHECK (tamanho_bytes > 0),
    origem VARCHAR(20) NOT NULL CHECK (origem IN ('PATROCINADOR', 'ADMIN')),
    data_hora_criacao TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
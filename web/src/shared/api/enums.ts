export type TipoUsuario = "ADMIN" | "PATROCINADOR";

export type StatusPatrocinador = "ATIVO" | "INATIVO";

export type CotaPatrocinio = "MASTER" | "SUBCOTA_G" | "SUBCOTA";

export type CategoriaDocumento =
  | "FORMULARIO_INGRESSOS"
  | "MIV"
  | "LOGOTIPO"
  | "FOTO_APLICACAO_MARCA"
  | "RELATORIO_MIDIA"
  | "PLANO_COMERCIAL"
  | "CONTRATO"
  | "OUTRO";

export type OrigemDocumento = "ADMIN" | "PATROCINADOR";

export type SetorCortesia = "ARENA" | "CAMAROTE" | "PREMIUM" | "ROOFTOP" | "TERRACO";

export type TipoCortesia = "SEXTA" | "SABADO" | "PASSAPORTE";

export type AcaoAuditoria =
  | "ADICIONOU_DOCUMENTO"
  | "REMOVEU_DOCUMENTO"
  | "BAIXOU_DOCUMENTO"
  | "ADICIONOU_PATROCINADOR"
  | "ALTEROU_PATROCINADOR"
  | "DESATIVOU_PATROCINADOR"
  | "ADICIONOU_PATROCINIO"
  | "REALIZOU_LOGIN"
  | "ADICIONOU_PESQUISA_ATIVACAO"
  | "ADICIONOU_CORTESIA"
  | "REMOVEU_CORTESIA";

export type EntidadeAuditoria =
  | "DOCUMENTO"
  | "PATROCINADOR"
  | "PATROCINIO"
  | "USUARIO"
  | "AUTENTICACAO"
  | "PESQUISA_ATIVACAO"
  | "CORTESIA";

export const COTAS: CotaPatrocinio[] = ["MASTER", "SUBCOTA_G", "SUBCOTA"];

export const CATEGORIAS_DOCUMENTO: CategoriaDocumento[] = [
  "FORMULARIO_INGRESSOS",
  "MIV",
  "LOGOTIPO",
  "FOTO_APLICACAO_MARCA",
  "RELATORIO_MIDIA",
  "PLANO_COMERCIAL",
  "CONTRATO",
  "OUTRO",
];

export const SETORES: SetorCortesia[] = ["ARENA", "CAMAROTE", "PREMIUM", "ROOFTOP", "TERRACO"];

export const TIPOS_CORTESIA: TipoCortesia[] = ["SEXTA", "SABADO", "PASSAPORTE"];

import type {
  AcaoAuditoria,
  CategoriaDocumento,
  CotaPatrocinio,
  OrigemDocumento,
  SetorCortesia,
  StatusPatrocinador,
  TipoCortesia,
  TipoUsuario,
} from "@/shared/api/enums";

export function formatarCnpj(cnpj: string | null | undefined) {
  const digits = (cnpj ?? "").replace(/\D/g, "");
  if (digits.length !== 14) return cnpj ?? "";
  return digits.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, "$1.$2.$3/$4-$5");
}

export function formatarTelefone(telefone: string | null | undefined) {
  const digits = (telefone ?? "").replace(/\D/g, "");
  if (digits.length === 11) return digits.replace(/^(\d{2})(\d{5})(\d{4})$/, "($1) $2-$3");
  if (digits.length === 10) return digits.replace(/^(\d{2})(\d{4})(\d{4})$/, "($1) $2-$3");
  return telefone ?? "";
}

export function formatarDataHora(valor: string | null | undefined) {
  if (!valor) return "—";
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(new Date(valor));
}

export function formatarData(valor: string | null | undefined) {
  if (!valor) return "—";
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  }).format(new Date(valor));
}

export function formatarTamanho(bytes: number | null | undefined) {
  if (!bytes && bytes !== 0) return "—";
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(0)} KB`;
  return `${(bytes / 1024 / 1024).toFixed(1)} MB`;
}

export function iniciais(nome: string | null | undefined) {
  if (!nome) return "?";
  return nome
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((parte) => parte[0]?.toUpperCase() ?? "")
    .join("");
}

export const rotuloCota: Record<CotaPatrocinio, string> = {
  MASTER: "Master",
  SUBCOTA_G: "Subcota G",
  SUBCOTA: "Subcota",
};

export const rotuloStatus: Record<StatusPatrocinador, string> = {
  ATIVO: "Ativo",
  INATIVO: "Inativo",
};

export const rotuloTipoUsuario: Record<TipoUsuario, string> = {
  ADMIN: "Admin RBS",
  PATROCINADOR: "Patrocinador",
};

export const rotuloCategoriaDocumento: Record<CategoriaDocumento, string> = {
  FORMULARIO_INGRESSOS: "Formulário de ingressos",
  MIV: "MIV",
  LOGOTIPO: "Logotipo",
  FOTO_APLICACAO_MARCA: "Foto de aplicação da marca",
  RELATORIO_MIDIA: "Relatório de mídia",
  PLANO_COMERCIAL: "Plano comercial",
  CONTRATO: "Contrato",
  OUTRO: "Outro",
};

export const rotuloOrigemDocumento: Record<OrigemDocumento, string> = {
  ADMIN: "Equipe RBS",
  PATROCINADOR: "Patrocinador",
};

export const rotuloSetor: Record<SetorCortesia, string> = {
  ARENA: "Arena",
  CAMAROTE: "Camarote",
  PREMIUM: "Premium",
  ROOFTOP: "Rooftop",
  TERRACO: "Terraço",
};

export const rotuloTipoCortesia: Record<TipoCortesia, string> = {
  SEXTA: "Sexta",
  SABADO: "Sábado",
  PASSAPORTE: "Passaporte",
};

export const rotuloAcaoAuditoria: Record<AcaoAuditoria, string> = {
  ADICIONOU_DOCUMENTO: "Adicionou documento",
  REMOVEU_DOCUMENTO: "Removeu documento",
  BAIXOU_DOCUMENTO: "Baixou documento",
  ADICIONOU_PATROCINADOR: "Adicionou patrocinador",
  ALTEROU_PATROCINADOR: "Alterou patrocinador",
  DESATIVOU_PATROCINADOR: "Desativou patrocinador",
  ADICIONOU_PATROCINIO: "Adicionou patrocínio",
  REALIZOU_LOGIN: "Realizou login",
  ADICIONOU_PESQUISA_ATIVACAO: "Adicionou pesquisa",
  ADICIONOU_CORTESIA: "Adicionou cortesia",
  REMOVEU_CORTESIA: "Removeu cortesia",
};

export function formatarCnpjInput(valor: string) {
  const digits = valor.replace(/\D/g, "").slice(0, 14);
  return digits
    .replace(/^(\d{2})(\d)/, "$1.$2")
    .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
    .replace(/\.(\d{3})(\d)/, ".$1/$2")
    .replace(/(\d{4})(\d)/, "$1-$2");
}

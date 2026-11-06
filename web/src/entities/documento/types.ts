import type { CategoriaDocumento, OrigemDocumento } from "@/shared/api/enums";

export type Documento = {
  id: number;
  nomeArquivo: string;
  categoria: CategoriaDocumento;
  tipoMime: string;
  tamanhoBytes: number;
  origem: OrigemDocumento;
  dataHoraCriacao: string;
};

export type EnviarDocumentoResponse = {
  id: number;
};

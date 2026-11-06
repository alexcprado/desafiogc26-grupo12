import type { CotaPatrocinio } from "@/shared/api/enums";
import type { Documento } from "@/entities/documento/types";
import type { Patrocinador } from "@/entities/patrocinador/types";

export type PatrocinioResumo = {
  id: number;
  anoEvento: number;
  cota: CotaPatrocinio;
  patrocinador: Patrocinador;
};

export type PatrocinioDetalhes = {
  idPatrocinio: number;
  anoEvento: number;
  cota: CotaPatrocinio;
  patrocinador: Patrocinador;
  documentos: Documento[];
};

export type AdicionarPatrocinioRequest = {
  idPatrocinador: number;
  cota: CotaPatrocinio;
};

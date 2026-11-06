import { api } from "@/shared/api/client";
import type { AdicionarCortesiaRequest, Cortesia } from "@/entities/cortesia/types";

export function listarCortesias(idPatrocinio: number) {
  return api<Cortesia[]>(`/patrocinios/${idPatrocinio}/cortesias`);
}

export function adicionarCortesia(idPatrocinio: number, corpo: AdicionarCortesiaRequest) {
  return api<{ idCortesia: number }>(`/patrocinios/${idPatrocinio}/cortesias`, {
    method: "POST",
    body: corpo,
  });
}

export function removerCortesia(idPatrocinio: number, idCortesia: number) {
  return api<number>(`/patrocinios/${idPatrocinio}/cortesias/${idCortesia}`, {
    method: "DELETE",
  });
}

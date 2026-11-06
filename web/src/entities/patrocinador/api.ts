import { api } from "@/shared/api/client";
import type { Pagina } from "@/shared/api/tipos";
import type {
  AdicionarPatrocinadorRequest,
  AlterarPatrocinadorRequest,
  ListarPatrocinadoresResponse,
  Patrocinador,
} from "@/entities/patrocinador/types";

export function listarPatrocinadores(pagina: number, tamanho = 10) {
  return api<Pagina<ListarPatrocinadoresResponse>>(
    `/patrocinadores?page=${pagina}&size=${tamanho}&sort=nome`,
  );
}

export function buscarPatrocinador(id: number) {
  return api<Patrocinador>(`/patrocinadores/${id}`);
}

export function adicionarPatrocinador(corpo: AdicionarPatrocinadorRequest) {
  return api<{ idPatrocinador: number }>("/patrocinadores", { method: "POST", body: corpo });
}

export function alterarPatrocinador(id: number, corpo: AlterarPatrocinadorRequest) {
  return api<{ idPatrocinador: number }>(`/patrocinadores/${id}`, { method: "PUT", body: corpo });
}

export function desativarPatrocinador(id: number) {
  return api<{ idPatrocinador: number }>(`/patrocinadores/${id}`, { method: "DELETE" });
}

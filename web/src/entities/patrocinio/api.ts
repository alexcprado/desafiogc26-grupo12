import { api } from "@/shared/api/client";
import type { Pagina } from "@/shared/api/tipos";
import type {
  AdicionarPatrocinioRequest,
  PatrocinioDetalhes,
  PatrocinioResumo,
} from "@/entities/patrocinio/types";

export function listarPatrocinios(
  pagina: number,
  tamanho: number,
  idEvento?: number | null,
) {
  const filtro = idEvento ? `&idEvento=${idEvento}` : "";
  return api<Pagina<PatrocinioResumo>>(
    `/patrocinios?page=${pagina}&size=${tamanho}&sort=id,desc${filtro}`,
  );
}

export function listarMeusPatrocinios() {
  return api<PatrocinioResumo[]>("/patrocinios/meus");
}

export function buscarDetalhesPatrocinio(id: number) {
  return api<PatrocinioDetalhes>(`/patrocinios/${id}/detalhes`);
}

export function adicionarPatrocinio(idEvento: number, corpo: AdicionarPatrocinioRequest) {
  return api<{ idPatrocinio: number }>(`/eventos/${idEvento}/patrocinios`, {
    method: "POST",
    body: corpo,
  });
}

export function gerarRelatorio(id: number) {
  return api<string>(`/patrocinios/${id}/relatorio`);
}

import { api } from "@/shared/api/client";
import type { Pagina } from "@/shared/api/tipos";
import type { Evento } from "@/entities/evento/types";

export function listarEventos() {
  return api<Pagina<Evento>>("/eventos?page=0&size=50&sort=ano,desc");
}

export function buscarEvento(id: number) {
  return api<Evento>(`/eventos/${id}`);
}

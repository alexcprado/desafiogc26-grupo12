import { api } from "@/shared/api/client";
import type { Pagina } from "@/shared/api/tipos";
import type { RegistroAuditoria } from "@/entities/auditoria/types";

export function listarAuditorias(pagina: number, tamanho = 15) {
  return api<Pagina<RegistroAuditoria>>(
    `/auditorias?page=${pagina}&size=${tamanho}&sort=dataHora,desc`,
  );
}

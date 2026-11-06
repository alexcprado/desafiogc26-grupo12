import { api } from "@/shared/api/client";
import type {
  AdicionarUsuarioAdminRequest,
  AdicionarUsuarioPatrocinadorRequest,
  AdicionarUsuarioResponse,
} from "@/entities/usuario/types";

export function adicionarUsuarioAdmin(corpo: AdicionarUsuarioAdminRequest) {
  return api<AdicionarUsuarioResponse>("/usuarios/admins", { method: "POST", body: corpo });
}

export function adicionarUsuarioPatrocinador(corpo: AdicionarUsuarioPatrocinadorRequest) {
  return api<AdicionarUsuarioResponse>("/usuarios/patrocinadores", {
    method: "POST",
    body: corpo,
  });
}

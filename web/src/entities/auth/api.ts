import { api } from "@/shared/api/client";
import type { LoginRequest, LoginResponse, UsuarioLogado } from "@/entities/auth/types";

export function entrar(email: string, senha: string) {
  const corpo: LoginRequest = { email, senha };
  return api<LoginResponse>("/auth/login", {
    method: "POST",
    body: corpo,
    auth: false,
  });
}

export function buscarUsuarioLogado() {
  return api<UsuarioLogado>("/auth/me");
}

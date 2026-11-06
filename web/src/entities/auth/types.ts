import type { TipoUsuario } from "@/shared/api/enums";

export type LoginRequest = {
  email: string;
  senha: string;
};

export type LoginResponse = {
  accessToken: string;
  expiresIn: number;
};

export type UsuarioLogado = {
  id: number;
  nome: string;
  email: string;
  tipo: TipoUsuario;
  idPatrocinador: number | null;
  nomePatrocinador: string | null;
};

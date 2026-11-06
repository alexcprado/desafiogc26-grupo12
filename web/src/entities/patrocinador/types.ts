import type { StatusPatrocinador } from "@/shared/api/enums";

export type Patrocinador = {
  id: number;
  nome: string;
  cnpj: string;
  segmento: string;
  contatoPrincipal: string;
  telefone: string;
  status: StatusPatrocinador;
};

export type ListarPatrocinadoresResponse = {
  id: number;
  nome: string;
  cnpj: string;
  segmento: string;
  contatoPrincipal: string;
  telefone: string;
  status: StatusPatrocinador;
};

export type AdicionarPatrocinadorRequest = {
  nome: string;
  cnpj: string;
  segmento: string;
  contatoPrincipal: string;
  telefone: string;
};

export type AlterarPatrocinadorRequest = {
  nome: string;
  segmento: string;
  contatoPrincipal: string;
  telefone: string;
};

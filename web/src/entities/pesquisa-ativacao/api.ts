import { api, ApiError } from "@/shared/api/client";
import type {
  AdicionarPesquisaAtivacaoRequest,
  PesquisaAtivacao,
} from "@/entities/pesquisa-ativacao/types";

export async function buscarPesquisaAtivacao(idPatrocinio: number) {
  try {
    return await api<PesquisaAtivacao>(`/patrocinios/${idPatrocinio}/pesquisa-ativacao`);
  } catch (erro) {
    if (erro instanceof ApiError && erro.status === 404) {
      return null;
    }
    throw erro;
  }
}

export function adicionarPesquisaAtivacao(
  idPatrocinio: number,
  corpo: AdicionarPesquisaAtivacaoRequest,
) {
  return api<{ id: number }>(`/patrocinios/${idPatrocinio}/pesquisa-ativacao`, {
    method: "POST",
    body: corpo,
  });
}

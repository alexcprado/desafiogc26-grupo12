import { api, ApiError, lerToken, urlApi } from "@/shared/api/client";
import type { CategoriaDocumento } from "@/shared/api/enums";
import type { Documento, EnviarDocumentoResponse } from "@/entities/documento/types";

export function enviarDocumento(
  idPatrocinio: number,
  arquivo: File,
  categoria: CategoriaDocumento,
) {
  const formulario = new FormData();
  formulario.append("arquivo", arquivo);
  formulario.append("categoria", categoria);

  return api<EnviarDocumentoResponse>(`/patrocinios/${idPatrocinio}/documentos`, {
    method: "POST",
    body: formulario,
  });
}

export async function baixarDocumento(
  idPatrocinio: number,
  documento: Pick<Documento, "id" | "nomeArquivo">,
) {
  const token = lerToken();
  const resposta = await fetch(
    urlApi(`/patrocinios/${idPatrocinio}/documentos/${documento.id}/download`),
    { headers: token ? { Authorization: `Bearer ${token}` } : {} },
  );

  if (!resposta.ok) {
    throw new ApiError(resposta.status, "Não foi possível baixar o arquivo.");
  }

  const blob = await resposta.blob();
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = documento.nomeArquivo;
  document.body.appendChild(link);
  link.click();
  link.remove();
  URL.revokeObjectURL(url);
}

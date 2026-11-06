import { useRef, useState } from "react";
import { Download, FileText, Loader2, Paperclip, Upload } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { CATEGORIAS_DOCUMENTO, type CategoriaDocumento } from "@/shared/api/enums";
import {
  formatarDataHora,
  formatarTamanho,
  rotuloCategoriaDocumento,
  rotuloOrigemDocumento,
} from "@/shared/lib/format";
import { Badge, Button, Select } from "@/shared/ui/primitives";
import { EmptyState } from "@/shared/ui/estados";
import { baixarDocumento, enviarDocumento } from "@/entities/documento/api";
import type { Documento } from "@/entities/documento/types";

export function DocumentosSecao({
  idPatrocinio,
  documentos,
  podeEnviar,
  aoAtualizar,
}: {
  idPatrocinio: number;
  documentos: Documento[];
  podeEnviar: boolean;
  aoAtualizar: () => void;
}) {
  const [categoria, setCategoria] = useState<CategoriaDocumento>("CONTRATO");
  const [arquivo, setArquivo] = useState<File | null>(null);
  const [enviando, setEnviando] = useState(false);
  const [baixando, setBaixando] = useState<number | null>(null);
  const [erro, setErro] = useState<string | null>(null);
  const campoArquivo = useRef<HTMLInputElement>(null);

  const categoriasDisponiveis = CATEGORIAS_DOCUMENTO.filter(
    (item) => !documentos.some((documento) => documento.categoria === item),
  );

  async function aoEnviar() {
    if (!arquivo) {
      setErro("Selecione um arquivo.");
      return;
    }

    setErro(null);
    setEnviando(true);

    try {
      await enviarDocumento(idPatrocinio, arquivo, categoria);
      setArquivo(null);
      if (campoArquivo.current) campoArquivo.current.value = "";
      aoAtualizar();
    } catch (falha) {
      setErro(
        falha instanceof ApiError ? falha.message : "Não foi possível enviar o documento.",
      );
    } finally {
      setEnviando(false);
    }
  }

  async function aoBaixar(documento: Documento) {
    setBaixando(documento.id);
    try {
      await baixarDocumento(idPatrocinio, documento);
    } catch {
      setErro("Não foi possível baixar o arquivo.");
    } finally {
      setBaixando(null);
    }
  }

  return (
    <div className="space-y-4">
      {podeEnviar ? (
        <div className="rounded-lg border border-border bg-card p-4">
          <div className="flex flex-wrap items-end gap-3">
            <div className="min-w-44 flex-1 space-y-1.5">
              <label className="text-xs font-medium text-muted-foreground" htmlFor="categoria">
                Categoria
              </label>
              <Select
                id="categoria"
                value={categoria}
                onChange={(evento) => setCategoria(evento.target.value as CategoriaDocumento)}
              >
                {categoriasDisponiveis.map((item) => (
                  <option key={item} value={item}>
                    {rotuloCategoriaDocumento[item]}
                  </option>
                ))}
              </Select>
            </div>

            <div className="min-w-52 flex-1 space-y-1.5">
              <label className="text-xs font-medium text-muted-foreground" htmlFor="arquivo">
                Arquivo (até 10 MB)
              </label>
              <label
                htmlFor="arquivo"
                className="flex h-9 cursor-pointer items-center gap-2 rounded-md border border-input bg-card px-3 text-xs text-muted-foreground transition-colors hover:border-[color-mix(in_oklab,var(--input)_70%,var(--graphite))] hover:text-foreground"
              >
                <Paperclip className="size-3.5 shrink-0" />
                <span className="truncate">{arquivo ? arquivo.name : "Selecionar arquivo"}</span>
              </label>
              <input
                ref={campoArquivo}
                id="arquivo"
                type="file"
                className="sr-only"
                onChange={(evento) => setArquivo(evento.target.files?.[0] ?? null)}
              />
            </div>

            <Button
              onClick={aoEnviar}
              disabled={enviando || !arquivo || categoriasDisponiveis.length === 0}
            >
              {enviando ? <Loader2 className="size-4 animate-spin" /> : <Upload className="size-4" />}
              Enviar
            </Button>
          </div>
          {categoriasDisponiveis.length === 0 ? (
            <p className="mt-2 text-xs text-muted-foreground">
              Todas as categorias já possuem um documento neste patrocínio.
            </p>
          ) : null}
          {erro ? <p className="mt-2 text-xs text-destructive">{erro}</p> : null}
        </div>
      ) : null}

      {documentos.length === 0 ? (
        <EmptyState
          titulo="Nenhum documento enviado"
          descricao="Contrato, MIV, logotipo e comprovantes aparecem aqui assim que forem enviados."
        />
      ) : (
        <ul className="divide-y divide-border rounded-lg border border-border bg-card">
          {documentos.map((documento) => (
            <li
              key={documento.id}
              className="flex flex-wrap items-center gap-3 px-4 py-3 transition-colors hover:bg-accent/40"
            >
              <div className="flex size-9 shrink-0 items-center justify-center rounded-md bg-muted text-muted-foreground">
                <FileText className="size-4" />
              </div>
              <div className="min-w-0 flex-1">
                <p className="truncate text-sm font-medium">{documento.nomeArquivo}</p>
                <p className="text-xs text-muted-foreground">
                  {rotuloCategoriaDocumento[documento.categoria]} ·{" "}
                  {formatarTamanho(documento.tamanhoBytes)} ·{" "}
                  {formatarDataHora(documento.dataHoraCriacao)}
                </p>
              </div>
              <Badge variante={documento.origem === "ADMIN" ? "brand" : "outline"}>
                {rotuloOrigemDocumento[documento.origem]}
              </Badge>
              <Button
                variante="outline"
                tamanho="sm"
                onClick={() => aoBaixar(documento)}
                disabled={baixando === documento.id}
              >
                {baixando === documento.id ? (
                  <Loader2 className="size-3.5 animate-spin" />
                ) : (
                  <Download className="size-3.5" />
                )}
                Baixar
              </Button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

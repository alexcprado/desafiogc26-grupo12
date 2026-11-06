import { useState } from "react";
import { Link } from "react-router-dom";
import { ArrowLeft, Download, FileText, Loader2, Sparkles } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { useAvisos } from "@/shared/ui/avisos";
import { Abas } from "@/shared/ui/abas";
import { Badge, Button } from "@/shared/ui/primitives";
import { PageHeader } from "@/shared/ui/estados";
import { formatarCnpj, formatarTelefone } from "@/shared/lib/format";
import { CotaBadge } from "@/entities/patrocinio/components/CotaBadge";
import { StatusPatrocinadorBadge } from "@/entities/patrocinador/components/StatusPatrocinadorBadge";
import { DocumentosSecao } from "@/entities/documento/components/DocumentosSecao";
import { CortesiasSecao } from "@/entities/cortesia/components/CortesiasSecao";
import { PesquisaSecao } from "@/entities/pesquisa-ativacao/components/PesquisaSecao";
import { gerarRelatorio } from "@/entities/patrocinio/api";
import type { PatrocinioDetalhes } from "@/entities/patrocinio/types";

const ABAS = [
  { valor: "visao", rotulo: "Visão geral" },
  { valor: "documentos", rotulo: "Documentos" },
  { valor: "cortesias", rotulo: "Cortesias" },
  { valor: "pesquisa", rotulo: "Pesquisa" },
];

export function PatrocinioPainel({
  detalhes,
  ehAdmin,
  aoAtualizar,
  voltarPara,
}: {
  detalhes: PatrocinioDetalhes;
  ehAdmin: boolean;
  aoAtualizar: () => void;
  voltarPara?: { para: string; rotulo: string };
}) {
  const [aba, setAba] = useState("visao");
  const [gerando, setGerando] = useState(false);
  const avisos = useAvisos();

  const { patrocinador } = detalhes;

  async function baixarRelatorio() {
    setGerando(true);
    try {
      const conteudo = await gerarRelatorio(detalhes.idPatrocinio);
      const blob = new Blob([conteudo], { type: "text/plain;charset=utf-8" });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `relatorio-${patrocinador.nome.toLowerCase().replace(/\s+/g, "-")}-${detalhes.anoEvento}.txt`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      URL.revokeObjectURL(url);
    } catch (falha) {
      avisos.erro(
        "Não foi possível gerar o relatório",
        falha instanceof ApiError ? falha.message : undefined,
      );
    } finally {
      setGerando(false);
    }
  }

  return (
    <div className="space-y-5">
      {voltarPara ? (
        <Link
          to={voltarPara.para}
          className="inline-flex items-center gap-1.5 text-xs text-muted-foreground hover:text-foreground"
        >
          <ArrowLeft className="size-3.5" /> {voltarPara.rotulo}
        </Link>
      ) : null}

      <PageHeader
        titulo={patrocinador.nome}
        descricao={`${patrocinador.segmento} · Edição ${detalhes.anoEvento}`}
        acoes={
          <>
            <StatusPatrocinadorBadge status={patrocinador.status} />
            <Button variante="outline" onClick={baixarRelatorio} disabled={gerando}>
              {gerando ? (
                <Loader2 className="size-4 animate-spin" />
              ) : (
                <Download className="size-4" />
              )}
              Baixar relatório
            </Button>
          </>
        }
      />

      <div className="flex flex-wrap items-center gap-2">
        <CotaBadge cota={detalhes.cota} />
        <Badge variante="neutral">
          <FileText className="size-3" />
          {detalhes.documentos.length}{" "}
          {detalhes.documentos.length === 1 ? "documento" : "documentos"}
        </Badge>
      </div>

      <Abas itens={ABAS} valor={aba} aoMudar={setAba} />

      <div className="animate-fade">
        {aba === "visao" ? (
          <div className="grid gap-4 lg:grid-cols-2">
            <div className="rounded-lg border border-border bg-card p-5">
              <h2 className="font-display text-sm font-semibold">Dados da marca</h2>
              <dl className="mt-4 grid gap-3 text-sm sm:grid-cols-2">
                <div>
                  <dt className="text-[11px] uppercase tracking-wide text-muted-foreground">CNPJ</dt>
                  <dd className="tabular-nums">{formatarCnpj(patrocinador.cnpj)}</dd>
                </div>
                <div>
                  <dt className="text-[11px] uppercase tracking-wide text-muted-foreground">
                    Segmento
                  </dt>
                  <dd>{patrocinador.segmento}</dd>
                </div>
                <div>
                  <dt className="text-[11px] uppercase tracking-wide text-muted-foreground">
                    Contato principal
                  </dt>
                  <dd>{patrocinador.contatoPrincipal}</dd>
                </div>
                <div>
                  <dt className="text-[11px] uppercase tracking-wide text-muted-foreground">
                    Telefone
                  </dt>
                  <dd className="tabular-nums">{formatarTelefone(patrocinador.telefone)}</dd>
                </div>
              </dl>
            </div>

            <div className="rounded-lg border border-border bg-card p-5">
              <h2 className="font-display text-sm font-semibold">Entregas do patrocínio</h2>
              <ul className="mt-4 space-y-3 text-sm">
                <li className="flex items-center justify-between gap-3">
                  <span className="flex items-center gap-2 text-muted-foreground">
                    <FileText className="size-4" /> Documentos enviados
                  </span>
                  <span className="font-medium tabular-nums">{detalhes.documentos.length}</span>
                </li>
                <li className="flex items-center justify-between gap-3">
                  <span className="flex items-center gap-2 text-muted-foreground">
                    <Sparkles className="size-4" /> Pesquisa de ativação
                  </span>
                  <span className="font-medium">
                    {detalhes.cota === "MASTER" ? "Disponível" : "Não prevista"}
                  </span>
                </li>
              </ul>
              <p className="mt-4 border-t border-border pt-3 text-xs text-muted-foreground">
                {ehAdmin
                  ? "Acesso administrativo — você pode enviar documentos e gerenciar cortesias."
                  : "Acesso liberado para a sua marca."}
              </p>
            </div>
          </div>
        ) : null}

        {aba === "documentos" ? (
          <DocumentosSecao
            idPatrocinio={detalhes.idPatrocinio}
            documentos={detalhes.documentos}
            podeEnviar
            aoAtualizar={aoAtualizar}
          />
        ) : null}

        {aba === "cortesias" ? (
          <CortesiasSecao idPatrocinio={detalhes.idPatrocinio} editavel={ehAdmin} />
        ) : null}

        {aba === "pesquisa" ? (
          <PesquisaSecao idPatrocinio={detalhes.idPatrocinio} cota={detalhes.cota} />
        ) : null}
      </div>
    </div>
  );
}

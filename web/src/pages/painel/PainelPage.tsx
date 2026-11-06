import type { ReactNode } from "react";
import { Link } from "react-router-dom";
import { ArrowRight, Building2, FileText, Handshake, LogIn, Sparkles, Ticket } from "lucide-react";
import { api } from "@/shared/api/client";
import type { Pagina } from "@/shared/api/tipos";
import { listarAuditorias } from "@/entities/auditoria/api";
import { listarEventos } from "@/entities/evento/api";
import { listarPatrocinadores } from "@/entities/patrocinador/api";
import { listarPatrocinios } from "@/entities/patrocinio/api";
import { CotaBadge } from "@/entities/patrocinio/components/CotaBadge";
import type { PesquisaAtivacao } from "@/entities/pesquisa-ativacao/types";
import { formatarDataHora, rotuloAcaoAuditoria } from "@/shared/lib/format";
import { useDados } from "@/shared/lib/use-dados";
import { Card, CardContent, CardHeader, CardTitle, Badge } from "@/shared/ui/primitives";
import { Carregando, ErrorState, PageHeader, StatCard } from "@/shared/ui/estados";
import { TBody, TD, TH, THead, TR, Table } from "@/shared/ui/table";

const iconesPorAcao: Record<string, ReactNode> = {
  REALIZOU_LOGIN: <LogIn className="size-3.5 text-brand" />,
  ADICIONOU_DOCUMENTO: <FileText className="size-3.5 text-highlight" />,
  BAIXOU_DOCUMENTO: <FileText className="size-3.5 text-muted-foreground" />,
  REMOVEU_DOCUMENTO: <FileText className="size-3.5 text-destructive" />,
  ADICIONOU_CORTESIA: <Ticket className="size-3.5 text-sun" />,
  REMOVEU_CORTESIA: <Ticket className="size-3.5 text-destructive" />,
};

function iconeDaAcao(acao: string) {
  return iconesPorAcao[acao] ?? <Building2 className="size-3.5 text-muted-foreground" />;
}

export function PainelPage() {
  const { dados, carregando, erro, recarregar } = useDados(async () => {
    const [eventos, patrocinadores, patrocinios, auditorias, pesquisas] = await Promise.all([
      listarEventos(),
      listarPatrocinadores(0, 1),
      listarPatrocinios(0, 6),
      listarAuditorias(0, 6),
      api<Pagina<PesquisaAtivacao>>("/patrocinios/pesquisa-ativacao?page=0&size=1"),
    ]);
    return { eventos, patrocinadores, patrocinios, auditorias, pesquisas };
  }, []);

  const eventoAtual = dados?.eventos.content[0];

  return (
    <div className="space-y-6">
      <PageHeader
        titulo="Visão Geral B2B"
        descricao="Acompanhe parceiros comerciais, cotas e as últimas movimentações do evento."
        acoes={
          eventoAtual ? (
            <Badge variante="success">
              <span className="size-1.5 rounded-full bg-success" />
              Planeta Atlântida {eventoAtual.ano} · Operação ativa
            </Badge>
          ) : null
        }
      />

      {carregando ? <Carregando linhas={4} /> : null}
      {erro ? <ErrorState mensagem={erro} aoTentarNovamente={recarregar} /> : null}

      {dados ? (
        <>
          <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
            <StatCard
              rotulo="Marcas ativas"
              valor={dados.patrocinadores.totalElements}
              detalhe="Patrocinadores com status ativo"
              icone={<Building2 className="size-4" />}
            />
            <StatCard
              rotulo="Patrocínios"
              valor={dados.patrocinios.totalElements}
              detalhe={`Vínculos com a edição ${eventoAtual?.ano ?? "atual"}`}
              icone={<Handshake className="size-4" />}
            />
            <StatCard
              rotulo="Pesquisas"
              valor={dados.pesquisas.totalElements}
              detalhe="Pesquisas de ativação registradas"
              icone={<Sparkles className="size-4" />}
            />
          </div>

          <div className="grid gap-4 lg:grid-cols-[1.5fr_1fr]">
            <Card>
              <CardHeader className="flex-row items-center justify-between">
                <CardTitle>Marcas parceiras</CardTitle>
                <Link
                  to="/patrocinios"
                  className="inline-flex items-center gap-1 text-xs font-medium text-brand-ink hover:underline"
                >
                  Ver todas <ArrowRight className="size-3.5" />
                </Link>
              </CardHeader>
              <CardContent className="px-0 pb-0 pt-3">
                {dados.patrocinios.content.length === 0 ? (
                  <p className="px-5 pb-5 text-sm text-muted-foreground">
                    Nenhum patrocínio cadastrado ainda.
                  </p>
                ) : (
                  <Table>
                    <THead>
                      <TH>Marca</TH>
                      <TH className="w-32 text-right">Cota</TH>
                    </THead>
                    <TBody>
                      {dados.patrocinios.content.map((patrocinio) => (
                        <TR key={patrocinio.id}>
                          <TD>
                            <Link
                              to={`/patrocinios/${patrocinio.id}`}
                              className="font-medium hover:text-brand-ink"
                            >
                              {patrocinio.patrocinador.nome}
                            </Link>
                            <p className="text-[11px] text-muted-foreground">
                              {patrocinio.patrocinador.segmento}
                            </p>
                          </TD>
                          <TD className="text-right">
                            <CotaBadge cota={patrocinio.cota} />
                          </TD>
                        </TR>
                      ))}
                    </TBody>
                  </Table>
                )}
              </CardContent>
            </Card>

            <Card className="flex flex-col">
              <CardHeader className="flex-row items-center justify-between">
                <CardTitle>Atividades recentes</CardTitle>
                <Link
                  to="/auditoria"
                  className="inline-flex items-center gap-1 text-xs font-medium text-brand-ink hover:underline"
                >
                  Ver auditoria <ArrowRight className="size-3.5" />
                </Link>
              </CardHeader>
              <CardContent className="flex-1 space-y-3 pt-3">
                {dados.auditorias.content.length === 0 ? (
                  <p className="text-sm text-muted-foreground">Sem movimentações registradas.</p>
                ) : (
                  <ul className="space-y-3">
                    {dados.auditorias.content.slice(0, 8).map((registro) => (
                      <li key={registro.id} className="flex items-start gap-2.5 text-xs">
                        <span className="mt-0.5 flex size-6 shrink-0 items-center justify-center rounded-md bg-muted">
                          {iconeDaAcao(registro.acao)}
                        </span>
                        <div className="min-w-0">
                          <p className="truncate text-foreground">
                            {registro.nomeRecurso || rotuloAcaoAuditoria[registro.acao]}
                          </p>
                          <p className="text-muted-foreground">
                            {registro.emailUsuario} · {formatarDataHora(registro.dataHora)}
                          </p>
                        </div>
                      </li>
                    ))}
                  </ul>
                )}
              </CardContent>
            </Card>
          </div>
        </>
      ) : null}
    </div>
  );
}

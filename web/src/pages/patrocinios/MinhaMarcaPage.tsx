import { useEffect, useState } from "react";
import { ShieldAlert } from "lucide-react";
import { useSessao } from "@/shared/auth/sessao";
import { useDados } from "@/shared/lib/use-dados";
import { Carregando, EmptyState, ErrorState, PageHeader } from "@/shared/ui/estados";
import { CotaBadge } from "@/entities/patrocinio/components/CotaBadge";
import { PatrocinioPainel } from "@/entities/patrocinio/components/PatrocinioPainel";
import { buscarDetalhesPatrocinio, listarMeusPatrocinios } from "@/entities/patrocinio/api";
import type { PatrocinioDetalhes, PatrocinioResumo } from "@/entities/patrocinio/types";
import { cn } from "@/shared/lib/cn";

export function MinhaMarcaPage() {
  const { usuario } = useSessao();
  const { dados, carregando, erro, recarregar } = useDados(() => listarMeusPatrocinios(), []);

  const [selecionado, setSelecionado] = useState<number | null>(null);
  const [versao, setVersao] = useState(0);
  const [detalhes, setDetalhes] = useState<PatrocinioDetalhes | null>(null);
  const [carregandoDetalhes, setCarregandoDetalhes] = useState(false);
  const [erroDetalhes, setErroDetalhes] = useState<string | null>(null);

  const idAtivo = selecionado ?? dados?.[0]?.id ?? null;

  useEffect(() => {
    if (!idAtivo) return;
    let ativo = true;
    setCarregandoDetalhes(true);

    buscarDetalhesPatrocinio(idAtivo)
      .then((resultado) => {
        if (ativo) {
          setDetalhes(resultado);
          setErroDetalhes(null);
        }
      })
      .catch(() => {
        if (ativo) setErroDetalhes("Não foi possível carregar os dados da sua marca.");
      })
      .finally(() => {
        if (ativo) setCarregandoDetalhes(false);
      });

    return () => {
      ativo = false;
    };
  }, [idAtivo, versao]);

  if (carregando) return <Carregando linhas={5} />;
  if (erro) return <ErrorState mensagem={erro} aoTentarNovamente={recarregar} />;

  if (!dados || dados.length === 0) {
    return (
      <div className="space-y-5">
        <PageHeader
          titulo={`Olá, ${usuario?.nome ?? ""}`}
          descricao="Seu acesso ainda não foi vinculado a uma marca."
        />
        <EmptyState
          titulo="Nenhum patrocínio vinculado"
          descricao="Fale com a equipe RBS para liberar o acesso da sua marca a esta edição do evento."
          acao={
            <span className="inline-flex items-center gap-1.5 text-xs text-muted-foreground">
              <ShieldAlert className="size-3.5" /> Acesso restrito por patrocinador
            </span>
          }
        />
      </div>
    );
  }

  if (carregandoDetalhes && !detalhes) {
    return <Carregando linhas={5} />;
  }

  if (erroDetalhes && !detalhes) {
    return <ErrorState mensagem={erroDetalhes} />;
  }

  if (!detalhes) return null;

  return (
    <div className="space-y-4">
      {dados.length > 1 ? (
        <div className="flex flex-wrap gap-2">
          {dados.map((patrocinio: PatrocinioResumo) => (
            <button
              key={patrocinio.id}
              onClick={() => setSelecionado(patrocinio.id)}
              className={cn(
                "flex cursor-pointer items-center gap-2 rounded-md border px-3 py-1.5 text-xs transition-colors",
                patrocinio.id === idAtivo
                  ? "border-brand/40 bg-brand-soft text-brand-ink"
                  : "border-border bg-card text-muted-foreground hover:text-foreground",
              )}
            >
              {patrocinio.anoEvento}
              <CotaBadge cota={patrocinio.cota} />
            </button>
          ))}
        </div>
      ) : null}

      <PatrocinioPainel
        detalhes={detalhes}
        ehAdmin={false}
        aoAtualizar={() => setVersao((atual) => atual + 1)}
      />
    </div>
  );
}

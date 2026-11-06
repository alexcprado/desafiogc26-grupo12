import type { ReactNode } from "react";
import { AlertTriangle, Inbox, RefreshCw } from "lucide-react";
import { cn } from "@/shared/lib/cn";
import { Button } from "@/shared/ui/primitives";

export function PageHeader({
  titulo,
  descricao,
  acoes,
  className,
}: {
  titulo: string;
  descricao?: string;
  acoes?: ReactNode;
  className?: string;
}) {
  return (
    <div className={cn("flex flex-wrap items-end justify-between gap-4", className)}>
      <div className="space-y-1">
        <h1 className="font-display text-2xl font-semibold tracking-tight text-balance">{titulo}</h1>
        {descricao ? (
          <p className="max-w-2xl text-sm text-muted-foreground">{descricao}</p>
        ) : null}
      </div>
      {acoes ? <div className="flex flex-wrap items-center gap-2">{acoes}</div> : null}
    </div>
  );
}

export function EmptyState({
  titulo,
  descricao,
  acao,
}: {
  titulo: string;
  descricao?: string;
  acao?: ReactNode;
}) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 rounded-lg border border-dashed border-border bg-card/40 px-6 py-12 text-center">
      <div className="flex size-10 items-center justify-center rounded-full bg-muted text-muted-foreground">
        <Inbox className="size-5" />
      </div>
      <div className="space-y-1">
        <p className="text-sm font-medium">{titulo}</p>
        {descricao ? <p className="text-xs text-muted-foreground">{descricao}</p> : null}
      </div>
      {acao}
    </div>
  );
}

export function ErrorState({
  mensagem,
  aoTentarNovamente,
}: {
  mensagem: string;
  aoTentarNovamente?: () => void;
}) {
  return (
    <div className="flex flex-col items-start gap-3 rounded-lg border border-destructive/35 bg-destructive/5 p-4">
      <div className="flex items-start gap-2">
        <AlertTriangle className="mt-0.5 size-4 text-destructive" />
        <p className="text-sm text-foreground">{mensagem}</p>
      </div>
      {aoTentarNovamente ? (
        <Button variante="outline" tamanho="sm" onClick={aoTentarNovamente}>
          <RefreshCw className="size-3.5" /> Tentar novamente
        </Button>
      ) : null}
    </div>
  );
}

export function Carregando({ linhas = 3 }: { linhas?: number }) {
  return (
    <div className="space-y-2" aria-busy="true" aria-live="polite">
      {Array.from({ length: linhas }).map((_, indice) => (
        <div
          key={indice}
          className="h-12 animate-pulse rounded-md border border-border/60 bg-muted/40"
        />
      ))}
    </div>
  );
}

export function StatCard({
  rotulo,
  valor,
  detalhe,
  icone,
}: {
  rotulo: string;
  valor: string | number;
  detalhe?: string;
  icone?: ReactNode;
}) {
  return (
    <div className="group relative overflow-hidden rounded-lg border border-border bg-card p-4">
      <div className="flex items-start justify-between gap-3">
        <div className="space-y-1">
          <p className="text-[11px] font-medium uppercase tracking-[0.09em] text-muted-foreground">
            {rotulo}
          </p>
          <p className="font-display text-2xl font-semibold tabular-nums tracking-tight">{valor}</p>
          {detalhe ? <p className="text-xs text-muted-foreground">{detalhe}</p> : null}
        </div>
        {icone ? (
          <div className="flex size-8 items-center justify-center rounded-md bg-brand-soft text-brand-ink">
            {icone}
          </div>
        ) : null}
      </div>
    </div>
  );
}

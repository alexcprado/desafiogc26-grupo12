import { cn } from "@/shared/lib/cn";

export type ItemAba = {
  valor: string;
  rotulo: string;
  contador?: number;
};

export function Abas({
  itens,
  valor,
  aoMudar,
  className,
}: {
  itens: ItemAba[];
  valor: string;
  aoMudar: (valor: string) => void;
  className?: string;
}) {
  return (
    <div
      role="tablist"
      className={cn(
        "flex w-full gap-1 overflow-x-auto rounded-lg border border-border bg-card p-1",
        className,
      )}
    >
      {itens.map((item) => {
        const ativo = item.valor === valor;
        return (
          <button
            key={item.valor}
            role="tab"
            aria-selected={ativo}
            onClick={() => aoMudar(item.valor)}
            className={cn(
              "inline-flex shrink-0 cursor-pointer items-center gap-2 rounded-md px-3 py-1.5 text-sm font-medium transition-colors",
              ativo
                ? "bg-brand-soft text-brand-ink shadow-sm ring-1 ring-brand/25"
                : "text-muted-foreground hover:bg-accent hover:text-foreground",
            )}
          >
            {item.rotulo}
            {typeof item.contador === "number" ? (
              <span
                className={cn(
                  "rounded-full px-1.5 py-0.5 text-[10px] tabular-nums",
                  ativo ? "bg-brand/20 text-brand-ink" : "bg-muted text-muted-foreground",
                )}
              >
                {item.contador}
              </span>
            ) : null}
          </button>
        );
      })}
    </div>
  );
}

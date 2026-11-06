import type { ReactNode } from "react";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { cn } from "@/shared/lib/cn";
import { Button } from "@/shared/ui/primitives";

export function Table({ className, children }: { className?: string; children: ReactNode }) {
  return (
    <div className={cn("w-full overflow-x-auto", className)}>
      <table className="w-full caption-bottom border-collapse text-sm">{children}</table>
    </div>
  );
}

export function THead({ children }: { children: ReactNode }) {
  return (
    <thead className="border-b border-border bg-muted/30">
      <tr className="text-left">{children}</tr>
    </thead>
  );
}

export function TH({ className, children }: { className?: string; children?: ReactNode }) {
  return (
    <th
      className={cn(
        "px-4 py-2.5 text-[11px] font-semibold uppercase tracking-[0.08em] text-muted-foreground",
        className,
      )}
    >
      {children}
    </th>
  );
}

export function TBody({ children }: { children: ReactNode }) {
  return <tbody className="divide-y divide-border/70">{children}</tbody>;
}

export function TR({
  className,
  children,
  onClick,
}: {
  className?: string;
  children: ReactNode;
  onClick?: () => void;
}) {
  return (
    <tr
      onClick={onClick}
      className={cn(
        "transition-colors",
        onClick && "cursor-pointer hover:bg-accent/60",
        className,
      )}
    >
      {children}
    </tr>
  );
}

export function TD({ className, children }: { className?: string; children?: ReactNode }) {
  return <td className={cn("px-4 py-3 align-middle", className)}>{children}</td>;
}

export function Paginacao({
  pagina,
  totalPaginas,
  totalElementos,
  aoMudar,
}: {
  pagina: number;
  totalPaginas: number;
  totalElementos: number;
  aoMudar: (pagina: number) => void;
}) {
  if (totalPaginas <= 1) return null;

  return (
    <div className="flex flex-wrap items-center justify-between gap-3 border-t border-border px-4 py-3 text-xs text-muted-foreground">
      <span>
        {totalElementos} {totalElementos === 1 ? "registro" : "registros"}
      </span>
      <div className="flex items-center gap-1">
        <Button
          variante="ghost"
          tamanho="icon"
          aria-label="Página anterior"
          disabled={pagina === 0}
          onClick={() => aoMudar(pagina - 1)}
        >
          <ChevronLeft className="size-4" />
        </Button>
        <span className="px-2 tabular-nums">
          {pagina + 1} / {totalPaginas}
        </span>
        <Button
          variante="ghost"
          tamanho="icon"
          aria-label="Próxima página"
          disabled={pagina >= totalPaginas - 1}
          onClick={() => aoMudar(pagina + 1)}
        >
          <ChevronRight className="size-4" />
        </Button>
      </div>
    </div>
  );
}

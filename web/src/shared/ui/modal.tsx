import { useEffect, type ReactNode } from "react";
import { X } from "lucide-react";
import { cn } from "@/shared/lib/cn";

export function Modal({
  aberto,
  titulo,
  descricao,
  aoFechar,
  children,
  className,
}: {
  aberto: boolean;
  titulo: string;
  descricao?: string;
  aoFechar: () => void;
  children: ReactNode;
  className?: string;
}) {
  useEffect(() => {
    if (!aberto) return;
    const aoTeclar = (evento: KeyboardEvent) => {
      if (evento.key === "Escape") aoFechar();
    };
    document.addEventListener("keydown", aoTeclar);
    document.body.style.overflow = "hidden";
    return () => {
      document.removeEventListener("keydown", aoTeclar);
      document.body.style.overflow = "";
    };
  }, [aberto, aoFechar]);

  if (!aberto) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center p-0 sm:items-center sm:p-6">
      <div
        className="absolute inset-0 bg-black/60 backdrop-blur-[2px] animate-fade"
        onClick={aoFechar}
        aria-hidden
      />
      <div
        role="dialog"
        aria-modal="true"
        aria-label={titulo}
        className={cn(
          "animate-rise relative z-10 max-h-[92dvh] w-full overflow-y-auto rounded-t-xl border border-border bg-card p-6 shadow-2xl sm:max-w-lg sm:rounded-lg",
          className,
        )}
      >
        <div className="mb-5 flex items-start justify-between gap-4">
          <div className="space-y-1">
            <h2 className="font-display text-lg font-semibold tracking-tight">{titulo}</h2>
            {descricao ? <p className="text-sm text-muted-foreground">{descricao}</p> : null}
          </div>
          <button
            onClick={aoFechar}
            aria-label="Fechar"
            className="cursor-pointer rounded-md p-1.5 text-muted-foreground transition-colors hover:bg-accent hover:text-foreground"
          >
            <X className="size-4" />
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}

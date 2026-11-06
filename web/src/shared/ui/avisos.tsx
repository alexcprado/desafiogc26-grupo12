import {
  createContext,
  useCallback,
  useContext,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { CheckCircle2, Info, TriangleAlert } from "lucide-react";
import { cn } from "@/shared/lib/cn";

type TipoAviso = "sucesso" | "erro" | "info";

type Aviso = {
  id: number;
  tipo: TipoAviso;
  titulo: string;
  descricao?: string;
};

type ContextoAvisos = {
  avisar: (aviso: Omit<Aviso, "id">) => void;
  sucesso: (titulo: string, descricao?: string) => void;
  erro: (titulo: string, descricao?: string) => void;
};

const AvisosContext = createContext<ContextoAvisos | null>(null);

const icones: Record<TipoAviso, ReactNode> = {
  sucesso: <CheckCircle2 className="size-4 text-success" />,
  erro: <TriangleAlert className="size-4 text-destructive" />,
  info: <Info className="size-4 text-brand" />,
};

export function AvisosProvider({ children }: { children: ReactNode }) {
  const [avisos, setAvisos] = useState<Aviso[]>([]);

  const avisar = useCallback((aviso: Omit<Aviso, "id">) => {
    const id = Date.now() + Math.random();
    setAvisos((atual) => [...atual, { ...aviso, id }]);
    window.setTimeout(() => {
      setAvisos((atual) => atual.filter((item) => item.id !== id));
    }, 5200);
  }, []);

  const valor = useMemo<ContextoAvisos>(
    () => ({
      avisar,
      sucesso: (titulo, descricao) => avisar({ tipo: "sucesso", titulo, descricao }),
      erro: (titulo, descricao) => avisar({ tipo: "erro", titulo, descricao }),
    }),
    [avisar],
  );

  return (
    <AvisosContext.Provider value={valor}>
      {children}
      <div className="pointer-events-none fixed bottom-4 right-4 z-[60] flex w-[min(92vw,22rem)] flex-col gap-2">
        {avisos.map((aviso) => (
          <div
            key={aviso.id}
            className={cn(
              "animate-rise pointer-events-auto flex items-start gap-3 rounded-lg border bg-card p-3 shadow-lg",
              aviso.tipo === "erro" ? "border-destructive/40" : "border-border",
            )}
          >
            <div className="mt-0.5">{icones[aviso.tipo]}</div>
            <div className="min-w-0 space-y-0.5">
              <p className="text-sm font-medium leading-snug">{aviso.titulo}</p>
              {aviso.descricao ? (
                <p className="text-xs leading-relaxed text-muted-foreground">{aviso.descricao}</p>
              ) : null}
            </div>
          </div>
        ))}
      </div>
    </AvisosContext.Provider>
  );
}

export function useAvisos() {
  const contexto = useContext(AvisosContext);
  if (!contexto) throw new Error("useAvisos deve ser usado dentro de AvisosProvider");
  return contexto;
}

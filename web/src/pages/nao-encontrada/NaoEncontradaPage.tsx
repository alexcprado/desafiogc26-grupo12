import { Link } from "react-router-dom";
import { Compass } from "lucide-react";
import { buttonClasses } from "@/shared/ui/primitives";

export function NaoEncontradaPage() {
  return (
    <div className="flex min-h-[60vh] flex-col items-center justify-center gap-4 text-center">
      <div className="flex size-12 items-center justify-center rounded-full bg-muted text-muted-foreground">
        <Compass className="size-6" />
      </div>
      <div className="space-y-1">
        <h1 className="font-display text-xl font-semibold">Página não encontrada</h1>
        <p className="text-sm text-muted-foreground">
          O endereço acessado não existe ou foi movido.
        </p>
      </div>
      <Link to="/painel" className={buttonClasses("outline", "md")}>
        Voltar ao painel
      </Link>
    </div>
  );
}

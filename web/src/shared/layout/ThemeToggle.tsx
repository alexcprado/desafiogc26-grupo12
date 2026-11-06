import { useEffect, useState } from "react";
import { Moon, Sun } from "lucide-react";
import { cn } from "@/shared/lib/cn";

const CHAVE = "planeta.tema";

type Tema = "claro" | "escuro";

function temaInicial(): Tema {
  const salvo = localStorage.getItem(CHAVE);
  if (salvo === "claro" || salvo === "escuro") return salvo;
  return "escuro";
}

export function aplicarTema(tema: Tema) {
  document.documentElement.classList.toggle("dark", tema === "escuro");
  localStorage.setItem(CHAVE, tema);
}

export function ThemeToggle({ className }: { className?: string }) {
  const [tema, setTema] = useState<Tema>(temaInicial);

  useEffect(() => {
    aplicarTema(tema);
  }, [tema]);

  const escuro = tema === "escuro";

  return (
    <button
      type="button"
      onClick={() => setTema(escuro ? "claro" : "escuro")}
      aria-label={escuro ? "Mudar para tema claro" : "Mudar para tema escuro"}
      title={escuro ? "Mudar para tema claro" : "Mudar para tema escuro"}
      className={cn(
        "flex cursor-pointer items-center gap-1.5 rounded-full px-2.5 py-1 text-xs font-medium transition-colors",
        "bg-sidebar-accent text-sidebar-foreground hover:bg-sidebar-accent/80",
        className,
      )}
    >
      {escuro ? (
        <Sun className="size-3.5 text-sun" />
      ) : (
        <Moon className="size-3.5 text-brand-ink" />
      )}
      <span>{escuro ? "Tema claro" : "Tema escuro"}</span>
    </button>
  );
}

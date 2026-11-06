import { useEffect, useState, type ReactNode } from "react";
import { NavLink, useLocation } from "react-router-dom";
import {
  Building2,
  ChartColumn,
  FileClock,
  Handshake,
  LogOut,
  Menu,
  ShieldCheck,
  Users,
  X,
} from "lucide-react";
import { cn } from "@/shared/lib/cn";
import { iniciais, rotuloTipoUsuario } from "@/shared/lib/format";
import { useSessao } from "@/shared/auth/sessao";
import { ThemeToggle } from "@/shared/layout/ThemeToggle";

type ItemNav = {
  para: string;
  rotulo: string;
  icone: ReactNode;
  exato?: boolean;
};

const itensAdmin: ItemNav[] = [
  { para: "/painel", rotulo: "Visão geral", icone: <ChartColumn className="size-4" />, exato: true },
  { para: "/patrocinadores", rotulo: "Patrocinadores", icone: <Building2 className="size-4" /> },
  { para: "/patrocinios", rotulo: "Patrocínios", icone: <Handshake className="size-4" /> },
  { para: "/auditoria", rotulo: "Auditoria", icone: <FileClock className="size-4" /> },
  { para: "/usuarios", rotulo: "Equipe RBS", icone: <Users className="size-4" /> },
];

const itensPatrocinador: ItemNav[] = [
  { para: "/minha-marca", rotulo: "Minha marca", icone: <ShieldCheck className="size-4" /> },
];

export function AppShell({ children }: { children: ReactNode }) {
  const { usuario, sair } = useSessao();
  const [aberto, setAberto] = useState(false);
  const local = useLocation();

  useEffect(() => {
    setAberto(false);
  }, [local.pathname]);

  const itens = usuario?.tipo === "ADMIN" ? itensAdmin : itensPatrocinador;

  return (
    <div className="min-h-dvh bg-background">
      <button
        type="button"
        onClick={() => setAberto(true)}
        aria-label="Abrir menu"
        className="fixed left-3 top-3 z-30 flex size-9 cursor-pointer items-center justify-center rounded-md border border-border bg-card text-foreground shadow-sm lg:hidden"
      >
        <Menu className="size-4" />
      </button>

      {aberto ? (
        <div
          className="fixed inset-0 z-30 bg-black/60 lg:hidden"
          onClick={() => setAberto(false)}
          aria-hidden
        />
      ) : null}

      <aside
        className={cn(
          "fixed inset-y-0 left-0 z-40 flex w-64 flex-col border-r border-sidebar-border bg-sidebar text-sidebar-foreground transition-transform duration-200",
          aberto ? "translate-x-0" : "-translate-x-full lg:translate-x-0",
        )}
      >
        <div className="flex items-center gap-2.5 px-4 py-4">
          <img
            src="/logo.png"
            alt=""
            width={36}
            height={36}
            className="aspect-square size-9 rounded-full object-contain"
          />
          <div className="min-w-0 leading-tight">
            <p className="truncate text-sm font-semibold">Portal do Patrocinador</p>
            <p className="truncate text-[11px] text-sidebar-muted">Planeta Atlântida</p>
          </div>
          <button
            type="button"
            onClick={() => setAberto(false)}
            aria-label="Fechar menu"
            className="ml-auto cursor-pointer rounded p-1 text-sidebar-muted hover:text-sidebar-foreground lg:hidden"
          >
            <X className="size-4" />
          </button>
        </div>

        <nav className="mt-2 flex-1 space-y-1 px-2">
          {itens.map((item) => (
            <NavLink
              key={item.para}
              to={item.para}
              end={item.exato}
              className={({ isActive }) =>
                cn(
                  "flex items-center gap-2.5 rounded-md px-3 py-2 text-sm transition-colors",
                  isActive
                    ? "bg-sidebar-accent font-medium text-sidebar-accent-foreground"
                    : "text-sidebar-muted hover:bg-sidebar-accent/60 hover:text-sidebar-foreground",
                )
              }
            >
              {item.icone}
              {item.rotulo}
            </NavLink>
          ))}
        </nav>

        <div className="space-y-3 border-t border-sidebar-border p-3">
          <ThemeToggle />
          <div className="flex items-center gap-2.5">
            <div className="flex size-8 shrink-0 items-center justify-center rounded-full bg-sidebar-accent text-[11px] font-semibold text-sidebar-accent-foreground">
              {iniciais(usuario?.nome)}
            </div>
            <div className="min-w-0 flex-1 leading-tight">
              <p className="truncate text-xs font-medium">{usuario?.nome}</p>
              <p className="truncate text-[11px] text-sidebar-muted">
                {usuario ? rotuloTipoUsuario[usuario.tipo] : ""}
              </p>
            </div>
            <button
              type="button"
              onClick={() => sair("manual")}
              aria-label="Sair"
              title="Sair"
              className="cursor-pointer rounded p-1.5 text-sidebar-muted transition-colors hover:bg-sidebar-accent hover:text-sidebar-foreground"
            >
              <LogOut className="size-4" />
            </button>
          </div>
        </div>
      </aside>

      <main className="lg:pl-64">
        <div className="mx-auto w-full max-w-6xl px-4 py-6 pt-14 sm:px-6 lg:pt-8">{children}</div>
      </main>
    </div>
  );
}

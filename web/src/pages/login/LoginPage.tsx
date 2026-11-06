import { Navigate } from "react-router-dom";
import { useSessao } from "@/shared/auth/sessao";
import { LoginForm } from "@/entities/auth/components/LoginForm";
import { ThemeToggle } from "@/shared/layout/ThemeToggle";

export function LoginPage() {
  const { usuario, carregando } = useSessao();

  if (carregando) {
    return <div className="min-h-dvh bg-background" />;
  }

  if (usuario) {
    return <Navigate to={usuario.tipo === "ADMIN" ? "/painel" : "/minha-marca"} replace />;
  }

  return (
    <div className="surface-grid flex min-h-dvh flex-col justify-between bg-background px-4 py-6">
      <div className="mx-auto flex w-full max-w-5xl items-center justify-between">
        <span className="text-sm text-muted-foreground">Grupo RBS · Planejamento B2B</span>
        <ThemeToggle />
      </div>

      <div className="my-auto flex justify-center py-8">
        <div className="w-full max-w-sm rounded-lg border border-border bg-card p-7 shadow-sm">
          <LoginForm />
        </div>
      </div>

      <p className="text-center text-[11px] text-muted-foreground">
        Planeta Atlântida · Gestão de patrocínios e entregas
      </p>
    </div>
  );
}

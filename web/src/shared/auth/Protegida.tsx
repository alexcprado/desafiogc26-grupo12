import { Navigate, Outlet } from "react-router-dom";
import type { TipoUsuario } from "@/shared/api/enums";
import { useSessao } from "@/shared/auth/sessao";
import { AppShell } from "@/shared/layout/AppShell";

export function Protegida({ papeis }: { papeis?: TipoUsuario[] }) {
  const { usuario, carregando } = useSessao();

  if (carregando) {
    return <div className="min-h-dvh bg-background" />;
  }

  if (!usuario) {
    return <Navigate to="/login" replace />;
  }

  if (papeis && !papeis.includes(usuario.tipo)) {
    return (
      <Navigate to={usuario.tipo === "ADMIN" ? "/painel" : "/minha-marca"} replace />
    );
  }

  return (
    <AppShell>
      <Outlet />
    </AppShell>
  );
}

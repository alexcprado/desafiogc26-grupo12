import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { useSessao, SessaoProvider } from "@/shared/auth/sessao";
import { Protegida } from "@/shared/auth/Protegida";
import { AvisosProvider } from "@/shared/ui/avisos";
import { LoginPage } from "@/pages/login/LoginPage";
import { PainelPage } from "@/pages/painel/PainelPage";
import { PatrocinadoresPage } from "@/pages/patrocinadores/PatrocinadoresPage";
import { PatrociniosPage } from "@/pages/patrocinios/PatrociniosPage";
import { PatrocinioDetalhePage } from "@/pages/patrocinios/PatrocinioDetalhePage";
import { MinhaMarcaPage } from "@/pages/patrocinios/MinhaMarcaPage";
import { AuditoriaPage } from "@/pages/auditoria/AuditoriaPage";
import { UsuariosPage } from "@/pages/usuarios/UsuariosPage";
import { NaoEncontradaPage } from "@/pages/nao-encontrada/NaoEncontradaPage";

function Raiz() {
  const { usuario, carregando } = useSessao();

  if (carregando) return <div className="min-h-dvh bg-background" />;
  if (!usuario) return <Navigate to="/login" replace />;

  return <Navigate to={usuario.tipo === "ADMIN" ? "/painel" : "/minha-marca"} replace />;
}

export function App() {
  return (
    <BrowserRouter>
      <SessaoProvider>
        <AvisosProvider>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/" element={<Raiz />} />

            <Route element={<Protegida papeis={["ADMIN"]} />}>
              <Route path="/painel" element={<PainelPage />} />
              <Route path="/patrocinadores" element={<PatrocinadoresPage />} />
              <Route path="/patrocinios" element={<PatrociniosPage />} />
              <Route path="/patrocinios/:id" element={<PatrocinioDetalhePage />} />
              <Route path="/auditoria" element={<AuditoriaPage />} />
              <Route path="/usuarios" element={<UsuariosPage />} />
            </Route>

            <Route element={<Protegida papeis={["PATROCINADOR"]} />}>
              <Route path="/minha-marca" element={<MinhaMarcaPage />} />
            </Route>

            <Route element={<Protegida />}>
              <Route path="*" element={<NaoEncontradaPage />} />
            </Route>
          </Routes>
        </AvisosProvider>
      </SessaoProvider>
    </BrowserRouter>
  );
}

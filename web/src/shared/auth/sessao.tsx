import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
  type ReactNode,
} from "react";
import { useNavigate } from "react-router-dom";
import { ApiError, gravarToken, lerToken, limparToken, registrarExpiracaoSessao } from "@/shared/api/client";
import { buscarUsuarioLogado, entrar } from "@/entities/auth/api";
import type { UsuarioLogado } from "@/entities/auth/types";

type Sessao = {
  usuario: UsuarioLogado | null;
  carregando: boolean;
  entrar: (email: string, senha: string) => Promise<UsuarioLogado>;
  sair: (motivo?: "expirou" | "manual") => void;
};

const SessaoContext = createContext<Sessao | null>(null);

function lerExpiracao(token: string) {
  try {
    const [, payload] = token.split(".");
    const json = JSON.parse(atob(payload.replace(/-/g, "+").replace(/_/g, "/")));
    return typeof json.exp === "number" ? json.exp * 1000 : null;
  } catch {
    return null;
  }
}

export function SessaoProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<UsuarioLogado | null>(null);
  const [carregando, setCarregando] = useState(Boolean(lerToken()));
  const navigate = useNavigate();
  const timer = useRef<number | null>(null);

  const sair = useCallback(
    (motivo: "expirou" | "manual" = "manual") => {
      limparToken();
      setUsuario(null);
      if (timer.current) window.clearTimeout(timer.current);
      navigate(`/login${motivo === "expirou" ? "?expirado=1" : ""}`, { replace: true });
    },
    [navigate],
  );

  const agendarExpiracao = useCallback(
    (token: string) => {
      if (timer.current) window.clearTimeout(timer.current);
      const expiraEm = lerExpiracao(token);
      if (!expiraEm) return;
      const restante = expiraEm - Date.now();
      if (restante <= 0) {
        sair("expirou");
        return;
      }
      timer.current = window.setTimeout(() => sair("expirou"), restante);
    },
    [sair],
  );

  useEffect(() => {
    registrarExpiracaoSessao(() => sair("expirou"));
  }, [sair]);

  useEffect(() => {
    const token = lerToken();
    if (!token) {
      setCarregando(false);
      return;
    }

    let ativo = true;

    buscarUsuarioLogado()
      .then((dados) => {
        if (!ativo) return;
        setUsuario(dados);
        agendarExpiracao(token);
      })
      .catch((erro: unknown) => {
        if (!ativo) return;
        if (erro instanceof ApiError && erro.status === 401) {
          sair("expirou");
        } else {
          setUsuario(null);
        }
      })
      .finally(() => {
        if (ativo) setCarregando(false);
      });

    return () => {
      ativo = false;
    };
  }, [agendarExpiracao, sair]);

  const autenticar = useCallback(
    async (email: string, senha: string) => {
      const resposta = await entrar(email, senha);
      gravarToken(resposta.accessToken);
      const dados = await buscarUsuarioLogado();
      setUsuario(dados);
      agendarExpiracao(resposta.accessToken);
      return dados;
    },
    [agendarExpiracao],
  );

  const valor = useMemo<Sessao>(
    () => ({ usuario, carregando, entrar: autenticar, sair }),
    [usuario, carregando, autenticar, sair],
  );

  return <SessaoContext.Provider value={valor}>{children}</SessaoContext.Provider>;
}

export function useSessao() {
  const contexto = useContext(SessaoContext);
  if (!contexto) {
    throw new Error("useSessao deve ser usado dentro de SessaoProvider");
  }
  return contexto;
}

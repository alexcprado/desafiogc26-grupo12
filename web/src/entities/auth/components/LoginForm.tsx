import { useState, type FormEvent } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { ArrowRight, Loader2 } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { useSessao } from "@/shared/auth/sessao";
import { Button, Field, Input } from "@/shared/ui/primitives";

export function LoginForm() {
  const { entrar } = useSessao();
  const navigate = useNavigate();
  const [parametros] = useSearchParams();
  const expirado = parametros.get("expirado") === "1";

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [enviando, setEnviando] = useState(false);

  async function aoSubmeter(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault();
    setErro(null);
    setEnviando(true);

    try {
      const usuario = await entrar(email.trim(), senha);
      navigate(usuario.tipo === "ADMIN" ? "/painel" : "/minha-marca", { replace: true });
    } catch (falha) {
      if (falha instanceof ApiError) {
        setErro(
          falha.status === 401
            ? "E-mail ou senha incorretos."
            : (falha.message ?? "Não foi possível entrar."),
        );
      } else {
        setErro("Falha de conexão com o servidor.");
      }
    } finally {
      setEnviando(false);
    }
  }

  return (
    <form className="w-full space-y-5" onSubmit={aoSubmeter}>
      <div>
        <div className="flex items-center gap-3">
          <img
            src="/logo.png"
            alt=""
            width={40}
            height={40}
            className="aspect-square size-10 shrink-0 rounded-full object-contain"
          />
          <div className="leading-tight">
            <span className="block text-sm font-semibold">Portal do Patrocinador</span>
            <span className="block text-xs text-muted-foreground">Planeta Atlântida</span>
          </div>
        </div>
        <h1 className="mt-5 font-display text-2xl font-semibold tracking-tight">Entrar</h1>
        <p className="mt-1 text-sm text-muted-foreground">Acesso restrito a e-mails liberados.</p>
      </div>

      {expirado ? (
        <div className="rounded-md border border-brand/30 bg-brand-soft px-3 py-2 text-xs text-brand-ink">
          Sua sessão expirou. Entre novamente para continuar.
        </div>
      ) : null}

      <div className="space-y-4">
        <Field label="E-mail" htmlFor="email">
          <Input
            id="email"
            type="email"
            autoComplete="email"
            placeholder="voce@empresa.com.br"
            required
            value={email}
            onChange={(evento) => setEmail(evento.target.value)}
          />
        </Field>

        <Field label="Senha" htmlFor="senha">
          <Input
            id="senha"
            type="password"
            autoComplete="current-password"
            placeholder="••••••••"
            required
            minLength={5}
            value={senha}
            onChange={(evento) => setSenha(evento.target.value)}
          />
        </Field>
      </div>

      {erro ? (
        <p role="alert" className="text-sm text-destructive">
          {erro}
        </p>
      ) : null}

      <Button type="submit" className="w-full" disabled={enviando}>
        {enviando ? <Loader2 className="size-4 animate-spin" /> : <ArrowRight className="size-4" />}
        {enviando ? "Entrando" : "Entrar"}
      </Button>

      <p className="text-center text-xs text-muted-foreground">
        Primeiro acesso e redefinição de senha são feitos pela equipe RBS.
      </p>
    </form>
  );
}

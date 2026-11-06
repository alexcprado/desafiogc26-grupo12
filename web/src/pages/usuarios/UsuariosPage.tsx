import { useState, type FormEvent } from "react";
import { Loader2, UserPlus } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { useDados } from "@/shared/lib/use-dados";
import { useAvisos } from "@/shared/ui/avisos";
import { Button, Card, CardContent, CardDescription, CardHeader, CardTitle, Field, Input, Select } from "@/shared/ui/primitives";
import { ErrorState, PageHeader } from "@/shared/ui/estados";
import { listarPatrocinadores } from "@/entities/patrocinador/api";
import { adicionarUsuarioAdmin, adicionarUsuarioPatrocinador } from "@/entities/usuario/api";

export function UsuariosPage() {
  const avisos = useAvisos();
  const patrocinadores = useDados(() => listarPatrocinadores(0, 100), []);

  const [admin, setAdmin] = useState({ nome: "", email: "", senha: "" });
  const [marca, setMarca] = useState({ nome: "", email: "", senha: "", idPatrocinador: "" });
  const [enviandoAdmin, setEnviandoAdmin] = useState(false);
  const [enviandoMarca, setEnviandoMarca] = useState(false);

  async function criarAdmin(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault();
    setEnviandoAdmin(true);
    try {
      await adicionarUsuarioAdmin(admin);
      avisos.sucesso("Admin criado", `A conta ${admin.email} já pode acessar o painel.`);
      setAdmin({ nome: "", email: "", senha: "" });
    } catch (falha) {
      avisos.erro(
        "Não foi possível criar o admin",
        falha instanceof ApiError ? falha.message : undefined,
      );
    } finally {
      setEnviandoAdmin(false);
    }
  }

  async function criarMarca(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault();
    setEnviandoMarca(true);
    try {
      await adicionarUsuarioPatrocinador({
        nome: marca.nome,
        email: marca.email,
        senha: marca.senha,
        idPatrocinador: Number(marca.idPatrocinador),
      });
      avisos.sucesso("Acesso da marca criado", `A conta ${marca.email} já pode entrar.`);
      setMarca({ nome: "", email: "", senha: "", idPatrocinador: "" });
    } catch (falha) {
      avisos.erro(
        "Não foi possível criar o acesso",
        falha instanceof ApiError ? falha.message : undefined,
      );
    } finally {
      setEnviandoMarca(false);
    }
  }

  return (
    <div className="space-y-5">
      <PageHeader
        titulo="Equipe RBS"
        descricao="Contas administrativas e acessos de marca. O login usa o e-mail cadastrado aqui."
      />

      {patrocinadores.erro ? (
        <ErrorState
          mensagem={patrocinadores.erro}
          aoTentarNovamente={patrocinadores.recarregar}
        />
      ) : null}

      <div className="grid gap-4 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Novo admin</CardTitle>
            <CardDescription>
              Administradores gerenciam eventos, patrocinadores, documentos e auditoria.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form className="space-y-3" onSubmit={criarAdmin}>
              <Field label="Nome" htmlFor="admin-nome">
                <Input
                  id="admin-nome"
                  required
                  minLength={3}
                  value={admin.nome}
                  onChange={(evento) => setAdmin({ ...admin, nome: evento.target.value })}
                />
              </Field>
              <Field label="E-mail corporativo" htmlFor="admin-email">
                <Input
                  id="admin-email"
                  type="email"
                  required
                  value={admin.email}
                  onChange={(evento) => setAdmin({ ...admin, email: evento.target.value })}
                  placeholder="nome@rbs.com.br"
                />
              </Field>
              <Field label="Senha inicial" htmlFor="admin-senha">
                <Input
                  id="admin-senha"
                  type="password"
                  required
                  minLength={6}
                  value={admin.senha}
                  onChange={(evento) => setAdmin({ ...admin, senha: evento.target.value })}
                />
              </Field>
              <div className="flex justify-end pt-1">
                <Button type="submit" disabled={enviandoAdmin}>
                  {enviandoAdmin ? (
                    <Loader2 className="size-4 animate-spin" />
                  ) : (
                    <UserPlus className="size-4" />
                  )}
                  Criar admin
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Acesso de marca</CardTitle>
            <CardDescription>
              Vincula uma pessoa a um patrocinador. Ela vê apenas o próprio patrocínio.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form className="space-y-3" onSubmit={criarMarca}>
              <Field label="Nome" htmlFor="marca-nome">
                <Input
                  id="marca-nome"
                  required
                  minLength={3}
                  value={marca.nome}
                  onChange={(evento) => setMarca({ ...marca, nome: evento.target.value })}
                />
              </Field>
              <Field label="E-mail" htmlFor="marca-email">
                <Input
                  id="marca-email"
                  type="email"
                  required
                  value={marca.email}
                  onChange={(evento) => setMarca({ ...marca, email: evento.target.value })}
                  placeholder="contato@marca.com.br"
                />
              </Field>
              <Field label="Senha inicial" htmlFor="marca-senha">
                <Input
                  id="marca-senha"
                  type="password"
                  required
                  minLength={6}
                  value={marca.senha}
                  onChange={(evento) => setMarca({ ...marca, senha: evento.target.value })}
                />
              </Field>
              <Field label="Patrocinador" htmlFor="marca-patrocinador">
                <Select
                  id="marca-patrocinador"
                  required
                  value={marca.idPatrocinador}
                  onChange={(evento) =>
                    setMarca({ ...marca, idPatrocinador: evento.target.value })
                  }
                >
                  <option value="">Selecione a marca</option>
                  {patrocinadores.dados?.content.map((item) => (
                    <option key={item.id} value={item.id}>
                      {item.nome}
                    </option>
                  ))}
                </Select>
              </Field>
              <div className="flex justify-end pt-1">
                <Button type="submit" disabled={enviandoMarca || !marca.idPatrocinador}>
                  {enviandoMarca ? (
                    <Loader2 className="size-4 animate-spin" />
                  ) : (
                    <UserPlus className="size-4" />
                  )}
                  Criar acesso
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

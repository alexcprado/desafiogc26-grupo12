import { useEffect, useState, type FormEvent } from "react";
import { Loader2 } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { formatarCnpj, formatarCnpjInput } from "@/shared/lib/format";
import { Button, Field, Input } from "@/shared/ui/primitives";
import { Modal } from "@/shared/ui/modal";
import { adicionarPatrocinador, alterarPatrocinador } from "@/entities/patrocinador/api";
import type { Patrocinador } from "@/entities/patrocinador/types";

type Props = {
  aberto: boolean;
  patrocinador: Patrocinador | null;
  aoFechar: () => void;
  aoSalvar: () => void;
};

type Campos = {
  nome: string;
  cnpj: string;
  segmento: string;
  contatoPrincipal: string;
  telefone: string;
};

const vazio: Campos = {
  nome: "",
  cnpj: "",
  segmento: "",
  contatoPrincipal: "",
  telefone: "",
};

export function PatrocinadorFormModal({ aberto, patrocinador, aoFechar, aoSalvar }: Props) {
  const [campos, setCampos] = useState<Campos>(vazio);
  const [erros, setErros] = useState<Record<string, string>>({});
  const [erroGeral, setErroGeral] = useState<string | null>(null);
  const [enviando, setEnviando] = useState(false);
  const editando = Boolean(patrocinador);

  useEffect(() => {
    if (!aberto) return;
    setCampos(
      patrocinador
        ? {
            nome: patrocinador.nome,
            cnpj: patrocinador.cnpj,
            segmento: patrocinador.segmento,
            contatoPrincipal: patrocinador.contatoPrincipal,
            telefone: patrocinador.telefone,
          }
        : vazio,
    );
    setErros({});
    setErroGeral(null);
  }, [aberto, patrocinador]);

  function atualizar(campo: keyof Campos, valor: string) {
    setCampos((atual) => ({ ...atual, [campo]: valor }));
  }

  async function aoSubmeter(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault();
    setEnviando(true);
    setErros({});
    setErroGeral(null);

    const cnpjDigitos = campos.cnpj.replace(/\D/g, "");

    try {
      if (editando && patrocinador) {
        await alterarPatrocinador(patrocinador.id, {
          nome: campos.nome.trim(),
          segmento: campos.segmento.trim(),
          contatoPrincipal: campos.contatoPrincipal.trim(),
          telefone: campos.telefone.trim(),
        });
      } else {
        await adicionarPatrocinador({
          nome: campos.nome.trim(),
          cnpj: cnpjDigitos,
          segmento: campos.segmento.trim(),
          contatoPrincipal: campos.contatoPrincipal.trim(),
          telefone: campos.telefone.trim(),
        });
      }
      aoSalvar();
    } catch (falha) {
      if (falha instanceof ApiError) {
        if (falha.campos) setErros(falha.campos);
        setErroGeral(falha.campos ? null : falha.message);
      } else {
        setErroGeral("Não foi possível salvar o patrocinador.");
      }
    } finally {
      setEnviando(false);
    }
  }

  return (
    <Modal
      aberto={aberto}
      titulo={editando ? "Editar patrocinador" : "Cadastrar patrocinador"}
      descricao={
        editando
          ? "O CNPJ não pode ser alterado depois do cadastro."
          : "Os dados aparecem no perfil da marca e nos relatórios do evento."
      }
      aoFechar={aoFechar}
    >
      <form className="space-y-4" onSubmit={aoSubmeter}>
        <Field label="Nome do patrocinador" htmlFor="nome" erro={erros.nome}>
          <Input
            id="nome"
            required
            minLength={3}
            maxLength={100}
            value={campos.nome}
            onChange={(evento) => atualizar("nome", evento.target.value)}
            placeholder="Ex.: Banrisul"
          />
        </Field>

        <div className="grid gap-4 sm:grid-cols-2">
          <Field label="CNPJ" htmlFor="cnpj" erro={erros.cnpj} hint="Somente números são enviados.">
            <Input
              id="cnpj"
              required
              inputMode="numeric"
              value={editando ? formatarCnpj(campos.cnpj) : campos.cnpj}
              disabled={editando}
              onChange={(evento) => atualizar("cnpj", formatarCnpjInput(evento.target.value))}
              placeholder="00.000.000/0000-00"
            />
          </Field>

          <Field label="Segmento" htmlFor="segmento" erro={erros.segmento}>
            <Input
              id="segmento"
              required
              value={campos.segmento}
              onChange={(evento) => atualizar("segmento", evento.target.value)}
              placeholder="Financeiro, Telecom, Varejo…"
            />
          </Field>

          <Field label="Contato principal" htmlFor="contato" erro={erros.contatoPrincipal}>
            <Input
              id="contato"
              required
              value={campos.contatoPrincipal}
              onChange={(evento) => atualizar("contatoPrincipal", evento.target.value)}
              placeholder="Nome de quem responde pela parceria"
            />
          </Field>

          <Field label="Telefone" htmlFor="telefone" erro={erros.telefone}>
            <Input
              id="telefone"
              required
              value={campos.telefone}
              onChange={(evento) => atualizar("telefone", evento.target.value)}
              placeholder="(51) 3215-7000"
            />
          </Field>
        </div>

        {erroGeral ? <p className="text-sm text-destructive">{erroGeral}</p> : null}

        <div className="flex justify-end gap-2 pt-1">
          <Button type="button" variante="ghost" onClick={aoFechar}>
            Cancelar
          </Button>
          <Button type="submit" disabled={enviando}>
            {enviando ? <Loader2 className="size-4 animate-spin" /> : null}
            {editando ? "Salvar alterações" : "Cadastrar patrocinador"}
          </Button>
        </div>
      </form>
    </Modal>
  );
}

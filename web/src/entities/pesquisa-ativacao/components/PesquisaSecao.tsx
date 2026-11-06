import { useEffect, useState } from "react";
import { Loader2, Lock, Send } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import type { CotaPatrocinio } from "@/shared/api/enums";
import { Button, Field, Textarea } from "@/shared/ui/primitives";
import { EmptyState } from "@/shared/ui/estados";
import { adicionarPesquisaAtivacao, buscarPesquisaAtivacao } from "@/entities/pesquisa-ativacao/api";
import type { PesquisaAtivacao } from "@/entities/pesquisa-ativacao/types";

export function PesquisaSecao({
  idPatrocinio,
  cota,
}: {
  idPatrocinio: number;
  cota: CotaPatrocinio;
}) {
  const [pesquisa, setPesquisa] = useState<PesquisaAtivacao | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [perguntas, setPerguntas] = useState(["", "", ""]);
  const [erro, setErro] = useState<string | null>(null);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    let ativo = true;
    setCarregando(true);

    buscarPesquisaAtivacao(idPatrocinio)
      .then((dados) => {
        if (ativo) setPesquisa(dados);
      })
      .catch(() => {
        if (ativo) setErro("Não foi possível carregar a pesquisa.");
      })
      .finally(() => {
        if (ativo) setCarregando(false);
      });

    return () => {
      ativo = false;
    };
  }, [idPatrocinio]);

  async function salvar() {
    setSalvando(true);
    setErro(null);

    try {
      await adicionarPesquisaAtivacao(idPatrocinio, {
        pergunta1: perguntas[0].trim(),
        pergunta2: perguntas[1].trim(),
        pergunta3: perguntas[2].trim(),
      });
      const dados = await buscarPesquisaAtivacao(idPatrocinio);
      setPesquisa(dados);
    } catch (falha) {
      setErro(falha instanceof ApiError ? falha.message : "Não foi possível salvar a pesquisa.");
    } finally {
      setSalvando(false);
    }
  }

  if (carregando) {
    return <p className="text-sm text-muted-foreground">Carregando pesquisa…</p>;
  }

  if (pesquisa) {
    return (
      <div className="space-y-4">
        <div className="flex items-start gap-2 rounded-lg border border-brand/25 bg-brand-soft/60 px-4 py-3 text-xs text-brand-ink">
          <Lock className="mt-0.5 size-3.5 shrink-0" />
          Perguntas enviadas. Depois de salvas, ficam bloqueadas para edição — fale com a equipe
          RBS se precisar de ajuste.
        </div>

        <ol className="space-y-3">
          {[pesquisa.pergunta1, pesquisa.pergunta2, pesquisa.pergunta3].map((pergunta, indice) => (
            <li
              key={indice}
              className="flex gap-3 rounded-lg border border-border bg-card p-4"
            >
              <span className="flex size-6 shrink-0 items-center justify-center rounded-full bg-brand-soft text-xs font-semibold text-brand-ink">
                {indice + 1}
              </span>
              <p className="text-sm leading-relaxed">{pergunta}</p>
            </li>
          ))}
        </ol>
      </div>
    );
  }

  if (cota !== "MASTER") {
    return (
      <EmptyState
        titulo="Pesquisa indisponível para esta cota"
        descricao="A pesquisa de ativação com três perguntas é exclusiva da cota Master."
      />
    );
  }

  return (
    <div className="space-y-4">
      <div className="rounded-lg border border-highlight/25 bg-highlight-soft/50 px-4 py-3 text-xs text-highlight-ink">
        Atenção: depois de salvar, as perguntas não poderão mais ser editadas.
      </div>

      <div className="space-y-3">
        {perguntas.map((valor, indice) => (
          <Field
            key={indice}
            label={`Pergunta ${indice + 1}`}
            htmlFor={`pergunta-${indice}`}
            hint="Pergunta direta, com no máximo 500 caracteres."
          >
            <Textarea
              id={`pergunta-${indice}`}
              rows={2}
              maxLength={500}
              value={valor}
              placeholder={`Digite a pergunta ${indice + 1} para o público do festival`}
              onChange={(evento) =>
                setPerguntas((atual) =>
                  atual.map((item, posicao) => (posicao === indice ? evento.target.value : item)),
                )
              }
            />
          </Field>
        ))}
      </div>

      {erro ? <p className="text-sm text-destructive">{erro}</p> : null}

      <div className="flex justify-end">
        <Button
          onClick={salvar}
          disabled={salvando || perguntas.some((pergunta) => pergunta.trim().length === 0)}
        >
          {salvando ? <Loader2 className="size-4 animate-spin" /> : <Send className="size-4" />}
          Salvar perguntas
        </Button>
      </div>
    </div>
  );
}

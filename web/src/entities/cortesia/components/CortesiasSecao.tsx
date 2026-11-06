import { useEffect, useState } from "react";
import { Loader2, Plus, Trash2 } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { SETORES, TIPOS_CORTESIA, type SetorCortesia, type TipoCortesia } from "@/shared/api/enums";
import { rotuloSetor, rotuloTipoCortesia } from "@/shared/lib/format";
import { Button, Field, Input, Select } from "@/shared/ui/primitives";
import { EmptyState } from "@/shared/ui/estados";
import { TBody, TD, TH, THead, TR, Table } from "@/shared/ui/table";
import { adicionarCortesia, listarCortesias, removerCortesia } from "@/entities/cortesia/api";
import type { Cortesia } from "@/entities/cortesia/types";

export function CortesiasSecao({
  idPatrocinio,
  editavel,
}: {
  idPatrocinio: number;
  editavel: boolean;
}) {
  const [cortesias, setCortesias] = useState<Cortesia[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [setor, setSetor] = useState<SetorCortesia>("ARENA");
  const [tipo, setTipo] = useState<TipoCortesia>("PASSAPORTE");
  const [quantidade, setQuantidade] = useState("10");
  const [salvando, setSalvando] = useState(false);
  const [removendo, setRemovendo] = useState<number | null>(null);

  async function carregar() {
    setCarregando(true);
    try {
      setCortesias(await listarCortesias(idPatrocinio));
      setErro(null);
    } catch {
      setErro("Não foi possível carregar as cortesias.");
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    void carregar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [idPatrocinio]);

  async function adicionar() {
    setSalvando(true);
    setErro(null);
    try {
      await adicionarCortesia(idPatrocinio, {
        setor,
        tipo,
        quantidade: Number(quantidade),
      });
      await carregar();
    } catch (falha) {
      setErro(falha instanceof ApiError ? falha.message : "Não foi possível adicionar a cortesia.");
    } finally {
      setSalvando(false);
    }
  }

  async function remover(id: number) {
    setRemovendo(id);
    try {
      await removerCortesia(idPatrocinio, id);
      await carregar();
    } catch (falha) {
      setErro(falha instanceof ApiError ? falha.message : "Não foi possível remover a cortesia.");
    } finally {
      setRemovendo(null);
    }
  }

  const total = cortesias.reduce((soma, item) => soma + item.quantidade, 0);

  return (
    <div className="space-y-4">
      {editavel ? (
        <div className="flex flex-wrap items-end gap-3 rounded-lg border border-border bg-card p-4">
          <Field label="Setor" htmlFor="setor" className="min-w-36 flex-1">
            <Select
              id="setor"
              value={setor}
              onChange={(evento) => setSetor(evento.target.value as SetorCortesia)}
            >
              {SETORES.map((item) => (
                <option key={item} value={item}>
                  {rotuloSetor[item]}
                </option>
              ))}
            </Select>
          </Field>

          <Field label="Tipo" htmlFor="tipo" className="min-w-36 flex-1">
            <Select
              id="tipo"
              value={tipo}
              onChange={(evento) => setTipo(evento.target.value as TipoCortesia)}
            >
              {TIPOS_CORTESIA.map((item) => (
                <option key={item} value={item}>
                  {rotuloTipoCortesia[item]}
                </option>
              ))}
            </Select>
          </Field>

          <Field label="Quantidade" htmlFor="quantidade" className="min-w-28">
            <Input
              id="quantidade"
              type="number"
              min={0}
              value={quantidade}
              onChange={(evento) => setQuantidade(evento.target.value)}
            />
          </Field>

          <Button onClick={adicionar} disabled={salvando}>
            {salvando ? <Loader2 className="size-4 animate-spin" /> : <Plus className="size-4" />}
            Adicionar
          </Button>
        </div>
      ) : null}

      {erro ? <p className="text-sm text-destructive">{erro}</p> : null}

      {carregando ? (
        <p className="text-sm text-muted-foreground">Carregando cortesias…</p>
      ) : cortesias.length === 0 ? (
        <EmptyState
          titulo="Nenhuma cortesia cadastrada"
          descricao="Quantidade e tipo por setor: passaporte, sexta ou sábado."
        />
      ) : (
        <div className="overflow-hidden rounded-lg border border-border bg-card">
          <Table>
            <THead>
              <TH>Setor</TH>
              <TH>Tipo</TH>
              <TH className="text-right">Quantidade</TH>
              {editavel ? <TH className="w-12" /> : null}
            </THead>
            <TBody>
              {cortesias.map((item) => (
                <TR key={item.id}>
                  <TD className="font-medium">{rotuloSetor[item.setor]}</TD>
                  <TD className="text-muted-foreground">{rotuloTipoCortesia[item.tipo]}</TD>
                  <TD className="text-right tabular-nums">{item.quantidade}</TD>
                  {editavel ? (
                    <TD className="text-right">
                      <Button
                        variante="ghost"
                        tamanho="icon"
                        aria-label={`Remover cortesia de ${rotuloSetor[item.setor]}`}
                        onClick={() => remover(item.id)}
                        disabled={removendo === item.id}
                      >
                        {removendo === item.id ? (
                          <Loader2 className="size-3.5 animate-spin" />
                        ) : (
                          <Trash2 className="size-3.5 text-muted-foreground" />
                        )}
                      </Button>
                    </TD>
                  ) : null}
                </TR>
              ))}
            </TBody>
          </Table>
          <div className="flex items-center justify-between border-t border-border px-4 py-2.5 text-xs text-muted-foreground">
            <span>Total de cortesias</span>
            <span className="font-medium tabular-nums text-foreground">{total}</span>
          </div>
        </div>
      )}
    </div>
  );
}

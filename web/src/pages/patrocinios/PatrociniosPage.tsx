import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { ArrowRight, Handshake, Loader2, Plus } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { COTAS, type CotaPatrocinio } from "@/shared/api/enums";
import { rotuloCota } from "@/shared/lib/format";
import { useDados } from "@/shared/lib/use-dados";
import { useAvisos } from "@/shared/ui/avisos";
import { Button, Field, Select } from "@/shared/ui/primitives";
import { Modal } from "@/shared/ui/modal";
import { Carregando, EmptyState, ErrorState, PageHeader } from "@/shared/ui/estados";
import { Paginacao, TBody, TD, TH, THead, TR, Table } from "@/shared/ui/table";
import { listarEventos } from "@/entities/evento/api";
import { listarPatrocinadores } from "@/entities/patrocinador/api";
import { adicionarPatrocinio, listarPatrocinios } from "@/entities/patrocinio/api";
import { CotaBadge } from "@/entities/patrocinio/components/CotaBadge";
import { StatusPatrocinadorBadge } from "@/entities/patrocinador/components/StatusPatrocinadorBadge";
import type { ListarPatrocinadoresResponse } from "@/entities/patrocinador/types";

const TAMANHO = 10;

export function PatrociniosPage() {
  const [pagina, setPagina] = useState(0);
  const [idEvento, setIdEvento] = useState<number | null>(null);
  const [modalAberto, setModalAberto] = useState(false);
  const avisos = useAvisos();

  const eventos = useDados(() => listarEventos(), []);
  const patrocinios = useDados(
    () => listarPatrocinios(pagina, TAMANHO, idEvento),
    [pagina, idEvento],
  );

  const eventoSelecionado =
    idEvento ?? eventos.dados?.content[0]?.id ?? null;

  useEffect(() => {
    if (idEvento === null && eventos.dados?.content[0]) {
      setIdEvento(eventos.dados.content[0].id);
    }
  }, [eventos.dados, idEvento]);

  return (
    <div className="space-y-5">
      <PageHeader
        titulo="Patrocínios"
        descricao="Vínculos entre marcas e a edição do evento, com a cota contratada em cada contrato."
        acoes={
          <Button onClick={() => setModalAberto(true)} disabled={!eventoSelecionado}>
            <Plus className="size-4" /> Vincular patrocinador
          </Button>
        }
      />

      <div className="flex flex-wrap items-center gap-3">
        <div className="w-44">
          <Select
            aria-label="Filtrar por evento"
            value={eventoSelecionado ?? ""}
            onChange={(evento) => {
              setIdEvento(Number(evento.target.value));
              setPagina(0);
            }}
          >
            {eventos.dados?.content.map((evento) => (
              <option key={evento.id} value={evento.id}>
                Edição {evento.ano}
              </option>
            ))}
          </Select>
        </div>
        {patrocinios.dados ? (
          <span className="text-xs text-muted-foreground">
            {patrocinios.dados.totalElements}{" "}
            {patrocinios.dados.totalElements === 1 ? "marca vinculada" : "marcas vinculadas"}
          </span>
        ) : null}
      </div>

      {patrocinios.carregando ? <Carregando linhas={5} /> : null}
      {patrocinios.erro ? (
        <ErrorState mensagem={patrocinios.erro} aoTentarNovamente={patrocinios.recarregar} />
      ) : null}

      {patrocinios.dados && !patrocinios.carregando ? (
        <div className="overflow-hidden rounded-lg border border-border bg-card">
          {patrocinios.dados.content.length === 0 ? (
            <div className="p-4">
              <EmptyState
                titulo="Nenhuma marca vinculada"
                descricao="Vincule um patrocinador já cadastrado a esta edição do evento."
              />
            </div>
          ) : (
            <Table>
              <THead>
                <TH>Marca</TH>
                <TH>Cota</TH>
                <TH>Status</TH>
                <TH className="w-10" />
              </THead>
              <TBody>
                {patrocinios.dados.content.map((patrocinio) => (
                  <TR key={patrocinio.id}>
                    <TD>
                      <Link
                        to={`/patrocinios/${patrocinio.id}`}
                        className="flex items-center gap-2.5 group"
                      >
                        <div className="flex size-8 shrink-0 items-center justify-center rounded-md bg-muted text-muted-foreground">
                          <Handshake className="size-4" />
                        </div>
                        <div className="min-w-0">
                          <p className="truncate text-sm font-medium group-hover:text-brand-ink">
                            {patrocinio.patrocinador.nome}
                          </p>
                          <p className="truncate text-[11px] text-muted-foreground">
                            {patrocinio.patrocinador.segmento}
                          </p>
                        </div>
                      </Link>
                    </TD>
                    <TD>
                      <CotaBadge cota={patrocinio.cota} />
                    </TD>
                    <TD>
                      <StatusPatrocinadorBadge status={patrocinio.patrocinador.status} />
                    </TD>
                    <TD className="text-right">
                      <Link
                        to={`/patrocinios/${patrocinio.id}`}
                        aria-label={`Abrir ${patrocinio.patrocinador.nome}`}
                        className="inline-flex text-muted-foreground hover:text-foreground"
                      >
                        <ArrowRight className="size-4" />
                      </Link>
                    </TD>
                  </TR>
                ))}
              </TBody>
            </Table>
          )}

          <Paginacao
            pagina={patrocinios.dados.number}
            totalPaginas={patrocinios.dados.totalPages}
            totalElementos={patrocinios.dados.totalElements}
            aoMudar={setPagina}
          />
        </div>
      ) : null}

      {eventoSelecionado ? (
        <VincularPatrocinadorModal
          aberto={modalAberto}
          idEvento={eventoSelecionado}
          aoFechar={() => setModalAberto(false)}
          aoVincular={() => {
            setModalAberto(false);
            avisos.sucesso("Patrocínio criado", "A marca já aparece na lista do evento.");
            patrocinios.recarregar();
          }}
        />
      ) : null}
    </div>
  );
}

function VincularPatrocinadorModal({
  aberto,
  idEvento,
  aoFechar,
  aoVincular,
}: {
  aberto: boolean;
  idEvento: number;
  aoFechar: () => void;
  aoVincular: () => void;
}) {
  const [patrocinadores, setPatrocinadores] = useState<ListarPatrocinadoresResponse[]>([]);
  const [idPatrocinador, setIdPatrocinador] = useState("");
  const [cota, setCota] = useState<CotaPatrocinio>("MASTER");
  const [erro, setErro] = useState<string | null>(null);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    if (!aberto) return;
    setErro(null);
    listarPatrocinadores(0, 100)
      .then((pagina) => {
        const ativos = pagina.content.filter((item) => item.status === "ATIVO");
        setPatrocinadores(ativos);
        setIdPatrocinador(ativos[0] ? String(ativos[0].id) : "");
      })
      .catch(() => setErro("Não foi possível carregar os patrocinadores."));
  }, [aberto]);

  async function salvar() {
    setSalvando(true);
    setErro(null);

    try {
      await adicionarPatrocinio(idEvento, {
        idPatrocinador: Number(idPatrocinador),
        cota,
      });
      aoVincular();
    } catch (falha) {
      setErro(falha instanceof ApiError ? falha.message : "Não foi possível vincular a marca.");
    } finally {
      setSalvando(false);
    }
  }

  return (
    <Modal
      aberto={aberto}
      titulo="Vincular patrocinador"
      descricao="A marca precisa já estar cadastrada. Cada patrocinador só pode ser vinculado uma vez ao mesmo evento."
      aoFechar={aoFechar}
    >
      <div className="space-y-4">
        <Field label="Patrocinador" htmlFor="patrocinador">
          <Select
            id="patrocinador"
            value={idPatrocinador}
            onChange={(evento) => setIdPatrocinador(evento.target.value)}
          >
            {patrocinadores.length === 0 ? (
              <option value="">Nenhum patrocinador ativo</option>
            ) : null}
            {patrocinadores.map((item) => (
              <option key={item.id} value={item.id}>
                {item.nome}
              </option>
            ))}
          </Select>
        </Field>

        <Field label="Cota" htmlFor="cota" hint="A cota define os direitos e a pesquisa de ativação.">
          <Select
            id="cota"
            value={cota}
            onChange={(evento) => setCota(evento.target.value as CotaPatrocinio)}
          >
            {COTAS.map((item) => (
              <option key={item} value={item}>
                {rotuloCota[item]}
              </option>
            ))}
          </Select>
        </Field>

        {erro ? <p className="text-sm text-destructive">{erro}</p> : null}

        <div className="flex justify-end gap-2 pt-1">
          <Button variante="ghost" onClick={aoFechar}>
            Cancelar
          </Button>
          <Button onClick={salvar} disabled={salvando || !idPatrocinador}>
            {salvando ? <Loader2 className="size-4 animate-spin" /> : null}
            Vincular
          </Button>
        </div>
      </div>
    </Modal>
  );
}

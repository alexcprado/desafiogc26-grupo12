import { useMemo, useState } from "react";
import { Building2, Pencil, Plus, Search, UserX } from "lucide-react";
import { ApiError } from "@/shared/api/client";
import { formatarCnpj, formatarTelefone } from "@/shared/lib/format";
import { useDados } from "@/shared/lib/use-dados";
import { useAvisos } from "@/shared/ui/avisos";
import { Button, Input } from "@/shared/ui/primitives";
import { Carregando, EmptyState, ErrorState, PageHeader } from "@/shared/ui/estados";
import { Paginacao, TBody, TD, TH, THead, TR, Table } from "@/shared/ui/table";
import { desativarPatrocinador, listarPatrocinadores } from "@/entities/patrocinador/api";
import { PatrocinadorFormModal } from "@/entities/patrocinador/components/PatrocinadorFormModal";
import { StatusPatrocinadorBadge } from "@/entities/patrocinador/components/StatusPatrocinadorBadge";
import type { ListarPatrocinadoresResponse } from "@/entities/patrocinador/types";

const TAMANHO = 10;

export function PatrocinadoresPage() {
  const [pagina, setPagina] = useState(0);
  const [busca, setBusca] = useState("");
  const [modalAberto, setModalAberto] = useState(false);
  const [selecionado, setSelecionado] = useState<ListarPatrocinadoresResponse | null>(null);
  const [desativando, setDesativando] = useState<number | null>(null);
  const avisos = useAvisos();

  const { dados, carregando, erro, recarregar } = useDados(
    () => listarPatrocinadores(pagina, TAMANHO),
    [pagina],
  );

  const filtrados = useMemo(() => {
    if (!dados) return [];
    const termo = busca.trim().toLowerCase();
    if (!termo) return dados.content;
    return dados.content.filter(
      (item) =>
        item.nome.toLowerCase().includes(termo) ||
        item.segmento?.toLowerCase().includes(termo) ||
        item.cnpj.includes(termo.replace(/\D/g, "")),
    );
  }, [dados, busca]);

  async function aoDesativar(patrocinador: ListarPatrocinadoresResponse) {
    const confirmar = window.confirm(
      `Desativar ${patrocinador.nome}? O histórico de patrocínios é preservado.`,
    );
    if (!confirmar) return;

    setDesativando(patrocinador.id);
    try {
      await desativarPatrocinador(patrocinador.id);
      avisos.sucesso("Patrocinador desativado", patrocinador.nome);
      recarregar();
    } catch (falha) {
      avisos.erro(
        "Não foi possível desativar",
        falha instanceof ApiError ? falha.message : undefined,
      );
    } finally {
      setDesativando(null);
    }
  }

  return (
    <div className="space-y-5">
      <PageHeader
        titulo="Patrocinadores"
        descricao="Marcas parceiras do evento, com dados de contato e status do vínculo comercial."
        acoes={
          <Button
            onClick={() => {
              setSelecionado(null);
              setModalAberto(true);
            }}
          >
            <Plus className="size-4" /> Novo patrocinador
          </Button>
        }
      />

      <div className="relative max-w-sm">
        <Search className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          className="pl-9"
          placeholder="Buscar por nome, segmento ou CNPJ"
          value={busca}
          onChange={(evento) => setBusca(evento.target.value)}
        />
      </div>

      {carregando ? <Carregando linhas={5} /> : null}
      {erro ? <ErrorState mensagem={erro} aoTentarNovamente={recarregar} /> : null}

      {dados && !carregando ? (
        <div className="overflow-hidden rounded-lg border border-border bg-card">
          {filtrados.length === 0 ? (
            <div className="p-4">
              <EmptyState
                titulo="Nenhum patrocinador encontrado"
                descricao={
                  busca
                    ? "Ajuste a busca ou cadastre uma nova marca."
                    : "Cadastre a primeira marca parceira do evento."
                }
                acao={
                  <Button
                    variante="outline"
                    tamanho="sm"
                    onClick={() => {
                      setSelecionado(null);
                      setModalAberto(true);
                    }}
                  >
                    <Plus className="size-3.5" /> Cadastrar patrocinador
                  </Button>
                }
              />
            </div>
          ) : (
            <Table>
              <THead>
                <TH>Marca</TH>
                <TH className="w-40">CNPJ</TH>
                <TH>Contato</TH>
                <TH className="w-24">Status</TH>
                <TH className="w-24 text-right">Ações</TH>
              </THead>
              <TBody>
                {filtrados.map((patrocinador) => (
                  <TR key={patrocinador.id}>
                    <TD>
                      <div className="flex items-center gap-2.5">
                        <div className="flex size-8 shrink-0 items-center justify-center rounded-md bg-muted text-muted-foreground">
                          <Building2 className="size-4" />
                        </div>
                        <div className="min-w-0">
                          <p className="truncate text-sm font-medium">{patrocinador.nome}</p>
                          <p className="truncate text-[11px] text-muted-foreground">
                            {patrocinador.segmento}
                          </p>
                        </div>
                      </div>
                    </TD>
                    <TD className="tabular-nums text-muted-foreground">
                      {formatarCnpj(patrocinador.cnpj)}
                    </TD>
                    <TD>
                      <p className="text-sm">{patrocinador.contatoPrincipal}</p>
                      <p className="text-[11px] text-muted-foreground">
                        {formatarTelefone(patrocinador.telefone)}
                      </p>
                    </TD>
                    <TD>
                      <StatusPatrocinadorBadge status={patrocinador.status} />
                    </TD>
                    <TD className="text-right">
                      <div className="flex justify-end gap-1">
                        <Button
                          variante="ghost"
                          tamanho="icon"
                          aria-label={`Editar ${patrocinador.nome}`}
                          title="Editar patrocinador"
                          onClick={() => {
                            setSelecionado(patrocinador);
                            setModalAberto(true);
                          }}
                        >
                          <Pencil className="size-3.5" />
                        </Button>
                        <Button
                          variante="ghost"
                          tamanho="icon"
                          aria-label={`Desativar ${patrocinador.nome}`}
                          title="Desativar patrocinador"
                          disabled={patrocinador.status === "INATIVO" || desativando === patrocinador.id}
                          onClick={() => aoDesativar(patrocinador)}
                        >
                          <UserX className="size-3.5 text-muted-foreground" />
                        </Button>
                      </div>
                    </TD>
                  </TR>
                ))}
              </TBody>
            </Table>
          )}

          <Paginacao
            pagina={dados.number}
            totalPaginas={dados.totalPages}
            totalElementos={dados.totalElements}
            aoMudar={setPagina}
          />
        </div>
      ) : null}

      <PatrocinadorFormModal
        aberto={modalAberto}
        patrocinador={selecionado}
        aoFechar={() => setModalAberto(false)}
        aoSalvar={() => {
          setModalAberto(false);
          avisos.sucesso(
            selecionado ? "Patrocinador atualizado" : "Patrocinador cadastrado",
            selecionado?.nome,
          );
          recarregar();
        }}
      />
    </div>
  );
}

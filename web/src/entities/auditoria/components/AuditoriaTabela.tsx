import { formatarDataHora, rotuloAcaoAuditoria } from "@/shared/lib/format";
import { Badge } from "@/shared/ui/primitives";
import { TBody, TD, TH, THead, TR, Table } from "@/shared/ui/table";
import type { RegistroAuditoria } from "@/entities/auditoria/types";

const corPorAcao: Record<string, "brand" | "highlight" | "neutral" | "success" | "danger"> = {
  ADICIONOU_DOCUMENTO: "brand",
  REMOVEU_DOCUMENTO: "danger",
  BAIXOU_DOCUMENTO: "neutral",
  ADICIONOU_PATROCINADOR: "success",
  ALTEROU_PATROCINADOR: "neutral",
  DESATIVOU_PATROCINADOR: "danger",
  ADICIONOU_PATROCINIO: "success",
  REALIZOU_LOGIN: "neutral",
  ADICIONOU_PESQUISA_ATIVACAO: "highlight",
  ADICIONOU_CORTESIA: "brand",
  REMOVEU_CORTESIA: "danger",
};

export function AuditoriaTabela({ registros }: { registros: RegistroAuditoria[] }) {
  return (
    <div className="overflow-hidden rounded-lg border border-border bg-card">
      <Table>
        <THead>
          <TH>Ação</TH>
          <TH>Recurso</TH>
          <TH>Usuário</TH>
          <TH className="text-right">Quando</TH>
        </THead>
        <TBody>
          {registros.map((registro) => (
            <TR key={registro.id}>
              <TD>
                <Badge variante={corPorAcao[registro.acao] ?? "neutral"}>
                  {rotuloAcaoAuditoria[registro.acao] ?? registro.acao}
                </Badge>
              </TD>
              <TD>
                <p className="text-sm">{registro.nomeRecurso || "—"}</p>
                <p className="text-[11px] uppercase tracking-wide text-muted-foreground">
                  {registro.entidade}
                </p>
              </TD>
              <TD className="text-sm text-muted-foreground">{registro.emailUsuario}</TD>
              <TD className="text-right text-xs text-muted-foreground">
                {formatarDataHora(registro.dataHora)}
              </TD>
            </TR>
          ))}
        </TBody>
      </Table>
    </div>
  );
}

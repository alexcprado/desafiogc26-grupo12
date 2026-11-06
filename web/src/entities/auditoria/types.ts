import type { AcaoAuditoria, EntidadeAuditoria } from "@/shared/api/enums";

export type RegistroAuditoria = {
  id: number;
  emailUsuario: string;
  acao: AcaoAuditoria;
  entidade: EntidadeAuditoria;
  nomeRecurso: string;
  dataHora: string;
};

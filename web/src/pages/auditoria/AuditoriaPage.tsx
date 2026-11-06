import { useState } from "react";
import { useDados } from "@/shared/lib/use-dados";
import { Carregando, EmptyState, ErrorState, PageHeader } from "@/shared/ui/estados";
import { Paginacao } from "@/shared/ui/table";
import { AuditoriaTabela } from "@/entities/auditoria/components/AuditoriaTabela";
import { listarAuditorias } from "@/entities/auditoria/api";

export function AuditoriaPage() {
  const [pagina, setPagina] = useState(0);
  const { dados, carregando, erro, recarregar } = useDados(
    () => listarAuditorias(pagina, 15),
    [pagina],
  );

  return (
    <div className="space-y-5">
      <PageHeader
        titulo="Auditoria"
        descricao="Histórico de logins, alterações de cadastro, documentos, cortesias e pesquisas."
      />

      {carregando ? <Carregando linhas={6} /> : null}
      {erro ? <ErrorState mensagem={erro} aoTentarNovamente={recarregar} /> : null}

      {dados && !carregando ? (
        dados.content.length === 0 ? (
          <EmptyState
            titulo="Sem registros"
            descricao="As ações administrativas aparecem aqui conforme acontecem."
          />
        ) : (
          <div className="overflow-hidden rounded-lg border border-border">
            <AuditoriaTabela registros={dados.content} />
            <Paginacao
              pagina={dados.number}
              totalPaginas={dados.totalPages}
              totalElementos={dados.totalElements}
              aoMudar={setPagina}
            />
          </div>
        )
      ) : null}
    </div>
  );
}

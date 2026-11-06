import { useParams } from "react-router-dom";
import { useSessao } from "@/shared/auth/sessao";
import { useDados } from "@/shared/lib/use-dados";
import { Carregando, ErrorState } from "@/shared/ui/estados";
import { buscarDetalhesPatrocinio } from "@/entities/patrocinio/api";
import { PatrocinioPainel } from "@/entities/patrocinio/components/PatrocinioPainel";

export function PatrocinioDetalhePage() {
  const { id } = useParams();
  const { usuario } = useSessao();
  const identificador = Number(id);

  const { dados, carregando, erro, recarregar } = useDados(
    () => buscarDetalhesPatrocinio(identificador),
    [identificador],
  );

  if (!dados && carregando) return <Carregando linhas={5} />;
  if (erro && !dados) return <ErrorState mensagem={erro} aoTentarNovamente={recarregar} />;
  if (!dados) return null;

  return (
    <PatrocinioPainel
      detalhes={dados}
      ehAdmin={usuario?.tipo === "ADMIN"}
      aoAtualizar={recarregar}
      voltarPara={{ para: "/patrocinios", rotulo: "Todos os patrocínios" }}
    />
  );
}

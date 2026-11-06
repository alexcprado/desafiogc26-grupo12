import { useCallback, useEffect, useState } from "react";
import { ApiError } from "@/shared/api/client";

type Estado<T> = {
  dados: T | null;
  carregando: boolean;
  erro: string | null;
  recarregar: () => void;
};

export function useDados<T>(carregar: () => Promise<T>, dependencias: unknown[]): Estado<T> {
  const [dados, setDados] = useState<T | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [versao, setVersao] = useState(0);

  const recarregar = useCallback(() => setVersao((atual) => atual + 1), []);

  useEffect(() => {
    let ativo = true;
    setCarregando(true);

    carregar()
      .then((resultado) => {
        if (!ativo) return;
        setDados(resultado);
        setErro(null);
      })
      .catch((falha: unknown) => {
        if (!ativo) return;
        setErro(
          falha instanceof ApiError
            ? falha.message
            : "Não foi possível carregar as informações.",
        );
      })
      .finally(() => {
        if (ativo) setCarregando(false);
      });

    return () => {
      ativo = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [...dependencias, versao]);

  return { dados, carregando, erro, recarregar };
}

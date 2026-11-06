import type { SetorCortesia, TipoCortesia } from "@/shared/api/enums";

export type Cortesia = {
  id: number;
  idPatrocinio: number;
  setor: SetorCortesia;
  tipo: TipoCortesia;
  quantidade: number;
};

export type AdicionarCortesiaRequest = {
  setor: SetorCortesia;
  tipo: TipoCortesia;
  quantidade: number;
};

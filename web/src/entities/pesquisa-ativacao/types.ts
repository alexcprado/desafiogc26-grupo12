export type PesquisaAtivacao = {
  id: number;
  idPatrocinio: number;
  pergunta1: string;
  pergunta2: string;
  pergunta3: string;
};

export type AdicionarPesquisaAtivacaoRequest = {
  pergunta1: string;
  pergunta2: string;
  pergunta3: string;
};

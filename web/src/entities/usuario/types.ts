export type AdicionarUsuarioAdminRequest = {
  nome: string;
  email: string;
  senha: string;
};

export type AdicionarUsuarioPatrocinadorRequest = AdicionarUsuarioAdminRequest & {
  idPatrocinador: number;
};

export type AdicionarUsuarioResponse = {
  id: number;
};

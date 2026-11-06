const API_BASE = import.meta.env.VITE_API_URL ?? "/api";

const TOKEN_KEY = "planeta.token";

export class ApiError extends Error {
  readonly status: number;
  readonly campos?: Record<string, string>;

  constructor(status: number, mensagem: string, campos?: Record<string, string>) {
    super(mensagem);
    this.name = "ApiError";
    this.status = status;
    this.campos = campos;
  }
}

type ErroApi = {
  message?: string;
  errors?: Record<string, string>;
  detail?: string;
};

let aoExpirarSessao: (() => void) | null = null;

export function registrarExpiracaoSessao(handler: () => void) {
  aoExpirarSessao = handler;
}

export function lerToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function gravarToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function limparToken() {
  localStorage.removeItem(TOKEN_KEY);
}

type Opcoes = Omit<RequestInit, "body"> & {
  body?: unknown;
  auth?: boolean;
};

export async function api<T>(caminho: string, opcoes: Opcoes = {}): Promise<T> {
  const { body, auth = true, headers, ...resto } = opcoes;
  const token = auth ? lerToken() : null;

  const resposta = await fetch(`${API_BASE}${caminho}`, {
    ...resto,
    headers: {
      ...(body instanceof FormData ? {} : { "Content-Type": "application/json" }),
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...headers,
    },
    body:
      body === undefined
        ? undefined
        : body instanceof FormData
          ? body
          : JSON.stringify(body),
  });

  if (resposta.status === 401) {
    if (auth) {
      aoExpirarSessao?.();
    }
    const erro = await lerErro(resposta);
    throw new ApiError(401, erro.message ?? "Sessão expirada. Entre novamente.");
  }

  if (!resposta.ok) {
    const erro = await lerErro(resposta);
    throw new ApiError(
      resposta.status,
      erro.message ?? erro.detail ?? "Não foi possível concluir a operação.",
      erro.errors,
    );
  }

  if (resposta.status === 204) {
    return undefined as T;
  }

  const tipo = resposta.headers.get("content-type") ?? "";
  if (tipo.includes("application/json")) {
    return (await resposta.json()) as T;
  }

  return (await resposta.text()) as T;
}

async function lerErro(resposta: Response): Promise<ErroApi> {
  try {
    const tipo = resposta.headers.get("content-type") ?? "";
    if (tipo.includes("application/json")) {
      return (await resposta.json()) as ErroApi;
    }
    const texto = await resposta.text();
    return { message: texto || undefined };
  } catch {
    return {};
  }
}

export function urlApi(caminho: string) {
  return `${API_BASE}${caminho}`;
}

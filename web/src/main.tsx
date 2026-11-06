import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "@/index.css";
import { App } from "@/app/App";
import { aplicarTema } from "@/shared/layout/ThemeToggle";

aplicarTema(localStorage.getItem("planeta.tema") === "claro" ? "claro" : "escuro");

const raiz = document.getElementById("root");

if (!raiz) {
  throw new Error("Elemento #root não encontrado");
}

createRoot(raiz).render(
  <StrictMode>
    <App />
  </StrictMode>,
);

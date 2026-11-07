import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import { fileURLToPath, URL } from "node:url";

// A API roda em 8080 por padrão (server.port no application.yml).
// Para outra porta, defina VITE_PROXY_TARGET antes de subir o dev server.
const destino = process.env.VITE_PROXY_TARGET ?? "http://localhost:8080";

const proxy = {
  "/api": {
    target: destino,
    changeOrigin: true,
    rewrite: (path: string) => path.replace(/^\/api/, ""),
  },
};

export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    port: 4200,
    allowedHosts: [".trycloudflare.com", "localhost"],
    proxy,
  },
  // `npm run preview` também precisa do proxy: sem ele o /api cai no index.html.
  preview: {
    port: 4173,
    proxy,
  },
});

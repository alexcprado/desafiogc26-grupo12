#!/usr/bin/env bash
# Smoke test do Portal do Patrocinador.
#
# Verifica, contra um ambiente já rodando:
#   1. a API responde em /actuator/health
#   2. o login funciona direto na API
#   3. o login funciona através do proxy do front (o caminho que o navegador usa)
#   4. o front entrega o index.html
#
# Uso:
#   ./verificar.sh
#   API_URL=http://localhost:8081 WEB_URL=http://localhost:4173 ./verificar.sh
#   EMAIL=fulano@empresa.com SENHA=senha ./verificar.sh
#
# Sai com 0 quando tudo passa e 1 quando algo falha.

set -u

API_URL=${API_URL:-http://localhost:8080}
WEB_URL=${WEB_URL:-http://localhost:4200}
EMAIL=${EMAIL:-admin@planeta.dev}
SENHA=${SENHA:-admin123}

falhas=0

ok() { printf '  ok    %s\n' "$1"; }
falhou() { printf '  FALHA %s\n' "$1"; falhas=$((falhas + 1)); }

codigo() { curl -s -o /dev/null -w '%{http_code}' --max-time 10 "$@"; }

echo "API: $API_URL"
echo "Web: $WEB_URL"
echo

echo "1. saúde da API"
if [ "$(codigo "$API_URL/actuator/health")" = "200" ]; then
  ok "GET /actuator/health"
else
  falhou "GET /actuator/health — a API está rodando em $API_URL?"
fi

echo "2. login direto na API"
status=$(codigo -X POST "$API_URL/auth/login" \
  -H 'Content-Type: application/json' \
  -d "{\"email\":\"$EMAIL\",\"senha\":\"$SENHA\"}")
if [ "$status" = "200" ]; then
  ok "POST /auth/login com $EMAIL"
else
  falhou "POST /auth/login devolveu $status (esperado 200)"
fi

echo "3. login pelo proxy do front"
status=$(codigo -X POST "$WEB_URL/api/auth/login" \
  -H 'Content-Type: application/json' \
  -d "{\"email\":\"$EMAIL\",\"senha\":\"$SENHA\"}")
if [ "$status" = "200" ]; then
  ok "POST $WEB_URL/api/auth/login"
else
  falhou "POST $WEB_URL/api/auth/login devolveu $status — confira o proxy do Vite (VITE_PROXY_TARGET) e a porta da API"
fi

echo "4. front"
if [ "$(codigo "$WEB_URL/login")" = "200" ]; then
  ok "GET /login"
else
  falhou "GET /login — o front está rodando em $WEB_URL?"
fi

echo
if [ "$falhas" -eq 0 ]; then
  echo "Tudo certo."
  exit 0
fi

echo "$falhas verificação(ões) falharam."
exit 1

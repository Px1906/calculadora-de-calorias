# Calculadora de Calorias

Sistema de controle de calorias em Clojure, dividido em duas partes que conversam entre si:

## back/ (`nota5`)
API REST (Compojure/Ring) responsável por:
- Cadastro do usuário (sexo, idade, peso) — `POST /cadastro`, `GET /cadastro`
- Registro de ganho calórico (alimentos) — `POST /ganho`
- Registro de perda calórica (atividades físicas) — `POST /perda`
- Consulta de extrato e saldo por período — `GET /extrato`, `GET /saldo`

Para calcular as calorias de verdade, a API consulta serviços externos (tradução, tabela nutricional e gasto calórico por atividade) em `src/nota5/apis.clj`.

Para rodar: `lein ring server` (sobe em `localhost:3001`).

## front/ (`calcapp`)
Cliente de linha de comando (CLI) que fala com a API acima: cadastro, lançamento de ganho/perda calórica e consulta de extrato.

Para rodar: `lein run` (depois de o back estar no ar).

## Pré-requisitos
[Leiningen](https://github.com/technomancy/leiningen) 2.0.0 ou superior.

# Calculadora de Calorias

Sistema de controle calórico em Clojure: registre o que você come e as atividades físicas que faz, e consulte o extrato e o saldo de calorias de um período. As calorias são calculadas a partir de APIs nutricionais externas.

![Status](https://img.shields.io/badge/status-conclu%C3%ADdo-brightgreen)
![Clojure](https://img.shields.io/badge/Clojure-1.11-5881d8)
![Leiningen](https://img.shields.io/badge/Leiningen-2.x-blue)
![Licença](https://img.shields.io/badge/licen%C3%A7a-EPL--2.0-green)

## Como funciona

```
┌──────────────────┐  HTTP/JSON     ┌──────────────────┐   HTTP    ┌────────────────────────┐
│ cli/ (terminal)  │ ─────────────▶ │ api/ (servidor)  │ ────────▶ │ APIs externas          │
│ calorias-cli     │ localhost:3001 │ calorias-api     │           │ tradução, nutrição e   │
└──────────────────┘                └──────────────────┘           │ gasto por atividade    │
                                                                   └────────────────────────┘
```

- **api/** (`calorias-api`): API REST com Compojure/Ring. Guarda o cadastro do usuário e os lançamentos em memória.
- **cli/** (`calorias-cli`): programa de terminal (CLI, *command-line interface*) com menu para cadastro, lançamentos e consultas. Ele é o cliente da API.

As rotas estão documentadas em [docs/API.md](docs/API.md).

## Requisitos

- Java 11 ou superior (testado com Java 17)
- [Leiningen](https://leiningen.org/) 2.x
- Chaves das APIs externas (veja [api/.env.example](api/.env.example))

## Como rodar

```bash
git clone https://github.com/Px1906/calculadora-de-calorias.git
cd calculadora-de-calorias
```

**1. Suba a API** (em um terminal):

```bash
cd api
export RAPIDAPI_TRANSLATE_KEY=...
export CALORIENINJAS_API_KEY=...
export RAPIDAPI_CALORIES_BURNED_KEY=...
lein ring server-headless     # sobe em http://localhost:3001
```

**2. Rode o CLI** (em outro terminal):

```bash
cd cli
lein run
```

O CLI usa `http://localhost:3001` por padrão. Para apontar para outro endereço, defina `CALORIAS_API_URL`.

## Testes

```bash
cd api && lein midje   # Midje
cd cli && lein test    # clojure.test
```

O CI (GitHub Actions) roda o lint com clj-kondo e os testes das duas partes em todo push e PR.

## Estrutura do projeto

```
.
├── api/                          # calorias-api
│   ├── src/calorias_api/
│   │   ├── handler.clj           # rotas
│   │   ├── lancamentos.clj       # regras puras: filtro, saldo, validação
│   │   ├── armazenamento.clj     # atoms (usuário e lançamentos)
│   │   └── servicos_externos.clj # tradução, nutrição, gasto calórico
│   └── test/
├── cli/                          # calorias-cli
│   ├── src/calorias_cli/
│   │   ├── core.clj              # menus
│   │   ├── cliente_api.clj       # chamadas HTTP
│   │   └── entrada.clj           # leitura e validação do teclado
│   └── test/
├── docs/                         # documentação da API
└── .github/                      # CI, templates de issue e PR
```

## Limitações conhecidas

- Os dados ficam em memória: reiniciar a API apaga cadastro e lançamentos.
- Só existe um usuário por vez.

## Contribuindo

Leia o [CONTRIBUTING.md](CONTRIBUTING.md) antes de abrir um PR.

## Histórico

Veja o [CHANGELOG](CHANGELOG.md).

## Licença

Distribuído sob a [Eclipse Public License 2.0](LICENSE).

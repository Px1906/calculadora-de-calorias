# API — calorias-api

Base: `http://localhost:3001`. Corpo das requisições e respostas em JSON.
Datas no formato ISO `yyyy-MM-dd` (o CLI converte a partir de `dd-MM-yyyy`).

| Método | Rota        | Descrição                                |
|--------|-------------|------------------------------------------|
| GET    | `/`         | Verifica se a API está no ar             |
| POST   | `/cadastro` | Define o usuário                         |
| GET    | `/cadastro` | Retorna o usuário cadastrado             |
| POST   | `/ganho`    | Registra um alimento consumido           |
| POST   | `/perda`    | Registra uma atividade física            |
| GET    | `/extrato`  | Lista os lançamentos de um período       |
| GET    | `/saldo`    | Ganhos menos perdas de um período        |

## POST /cadastro

```json
{ "sexo": 1, "idade": 25, "peso": 70 }
```

`sexo`: `1` masculino, `2` feminino. Responde com o usuário salvo.

## POST /ganho

```json
{ "alimento": "arroz", "quantidade": 200, "data": "2026-09-21" }
```

`quantidade` em gramas. O alimento é traduzido para inglês e consultado na CalorieNinjas.
Responde com a lista completa de lançamentos.

Se faltar algum campo, a quantidade não for positiva ou a data não estiver em `yyyy-MM-dd`,
responde `400` com `{ "erro": "..." }` e não registra nada.

## POST /perda

```json
{ "atividade": "correr", "duracao": 30, "data": "2026-09-21" }
```

`duracao` em minutos. Usa o `peso` do cadastro: sem cadastro, responde `400`.
Dados inválidos também respondem `400` com `{ "erro": "..." }`.
Responde com a lista completa de lançamentos.

## GET /extrato?inicio=2026-09-01&fim=2026-09-30

Lançamentos com data entre `inicio` e `fim` (inclusive):

```json
[
  { "tipo": "ganho", "comida": "arroz", "quantidade": 200, "calorias": 254.6, "data": "2026-09-21" },
  { "tipo": "perda", "atividade": "correr", "duracao": 30, "calorias": 311, "data": "2026-09-21" }
]
```

## GET /saldo?inicio=2026-09-01&fim=2026-09-30

Um número: soma das calorias de `ganho` menos as de `perda` no período.

## Variáveis de ambiente

| Variável                       | Uso                                   |
|--------------------------------|---------------------------------------|
| `RAPIDAPI_TRANSLATE_KEY`       | Tradução pt → en (Deep Translate)     |
| `CALORIENINJAS_API_KEY`        | Calorias de alimentos (CalorieNinjas) |
| `RAPIDAPI_CALORIES_BURNED_KEY` | Gasto por atividade (API Ninjas)      |

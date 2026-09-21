# calorias-api

API REST (Clojure) do backend da calculadora de calorias.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Configuração

Antes de rodar, defina as variáveis de ambiente com as chaves das APIs externas
(veja `.env.example`):

- `RAPIDAPI_TRANSLATE_KEY`
- `CALORIENINJAS_API_KEY`
- `RAPIDAPI_CALORIES_BURNED_KEY`

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2025 FIXME

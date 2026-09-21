# calorias-api

API REST (Clojure) do backend da calculadora de calorias.

## Prerequisites

Voce precisara do [Leiningen][] 2.0.0 ou acima instalado.

[leiningen]: https://github.com/technomancy/leiningen

## Configuração

Antes de rodar, defina as variáveis de ambiente com as chaves das APIs externas
(veja `.env.example`):

- `RAPIDAPI_TRANSLATE_KEY`
- `CALORIENINJAS_API_KEY`
- `RAPIDAPI_CALORIES_BURNED_KEY`

## Rodar

Para começar o servidor na web para a aplicação, rode:

    lein ring server

## Licença

Copyright © 2025 FIXME

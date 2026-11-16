# Contribuindo

## Fluxo de trabalho

1. Crie uma issue descrevendo o que será feito (ou comente numa existente).
2. Crie uma branch a partir de `master`: `feat/nome`, `fix/nome` ou `chore/nome`.
3. Faça commits pequenos e focados.
4. Abra um Pull Request preenchendo o template.
5. Aguarde o CI passar e uma revisão antes do merge.

`master` está sempre funcionando. Não faça push direto nela.

## Mensagens de commit

Seguimos [Conventional Commits](https://www.conventionalcommits.org/pt-br/):

```
feat: adiciona rota de exclusão de lançamento
fix: corrige saldo quando o período está vazio
docs: documenta a rota /extrato
refactor: separa leitura de entrada do menu
test: cobre o filtro de período
chore: atualiza dependências da api
```

## Convenções de código (Clojure)

Seguimos o [Clojure Style Guide](https://guide.clojure.style/):

- Namespaces em `kebab-case` (`calorias-api.servicos-externos`); arquivos em `snake_case` (`servicos_externos.clj`)
- Funções e variáveis em `kebab-case` e em português, como no resto do código
- Predicados terminam em `?` (`ganho-valido?`); funções que alteram estado ou fazem requisições terminam em `!` (`salvar-usuario!`)
- Indentação com 2 espaços
- Nada de URLs ou chaves fixas no código: use variáveis de ambiente
- Sem código comentado nem rotas de teste esquecidas no `handler.clj`

## Onde colocar cada coisa

| Se o código...                                 | vai em                              |
|------------------------------------------------|-------------------------------------|
| define uma rota ou monta a resposta HTTP       | `api/.../handler.clj`               |
| é regra de negócio (calcular, filtrar, validar)| `api/.../lancamentos.clj` (só funções puras) |
| lê ou grava os dados guardados                 | `api/.../armazenamento.clj`         |
| chama uma API de terceiros                     | `api/.../servicos_externos.clj`     |
| mostra menus e textos no terminal              | `cli/.../core.clj`                  |
| faz requisição para a nossa API                | `cli/.../cliente_api.clj`           |
| lê e valida o que o usuário digita             | `cli/.../entrada.clj`               |

Regras puras não fazem HTTP, não imprimem e não mexem em `atom`: assim dá para testá-las sem servidor nem mocks.

Rode o lint antes de abrir o PR:

```bash
clj-kondo --lint api/src api/test cli/src cli/test
```

## Segredos

As chaves das APIs externas vão **apenas** em variáveis de ambiente ou num `.env` local (já ignorado pelo Git). Veja `api/.env.example`. Se uma chave vazar num commit, revogue-a no provedor imediatamente.

## API

Mudou ou criou uma rota? Atualize [docs/API.md](docs/API.md) no mesmo PR.

## Testes

- **api**: [Midje](https://github.com/marick/Midje), em `api/test/`. Rode `lein midje`.
- **cli**: `clojure.test`, em `cli/test/`. Rode `lein test`.

Testes não devem chamar as APIs externas de verdade: use `with-redefs` para simular as respostas.

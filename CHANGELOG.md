# Changelog

Formato baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.1.0/),
versionamento seguindo [SemVer](https://semver.org/lang/pt-BR/).

## [Não lançado]

### Adicionado
- `CONTRIBUTING.md`, `.editorconfig`, templates de issue e de PR.
- CI no GitHub Actions com lint (clj-kondo) e testes da API e do CLI.
- Documentação das rotas em `docs/API.md`.
- Validação dos lançamentos na API: `POST /ganho` e `POST /perda` com dados inválidos respondem `400` com `{"erro": ...}`, e `POST /perda` sem cadastro também.
- Testes da API: regras de lançamento (filtro, saldo, validação) e rotas de ponta a ponta com `ring-mock`, com as APIs externas simuladas.
- Testes do CLI para a leitura e validação da entrada do usuário.
- Endereço da API configurável no CLI pela variável `CALORIAS_API_URL`.

### Alterado
- Pastas renomeadas: `back/` → `api/` e `front/` → `cli/`. O projeto do cliente passa de `calcapp` para `calorias-cli`.
- API reorganizada por responsabilidade: `handler.clj` (rotas), `lancamentos.clj` (regras puras), `armazenamento.clj` (estado em memória) e `servicos_externos.clj` (antigo `apis.clj`, agora devolve direto o número de calorias). O antigo `banco_de_dados.clj` foi dividido entre eles.
- CLI reorganizado: `core.clj` (menus), `cliente_api.clj` (chamadas HTTP) e `entrada.clj` (antigo `tratamentos.clj`).
- Versões alinhadas entre os dois projetos: Clojure 1.11.1 e clojure.java-time 1.4.2. Midje atualizado para 1.10.10.
- README reescrito com arquitetura, requisitos, como rodar, testes e estrutura.
- Licença movida para a raiz do repositório.
- O menu de cadastro lê a opção com validação, como os demais menus.

### Removido
- Rotas de depuração `/testeGanho` e `/testePerda`, que chamavam APIs pagas e devolviam sempre `null`.
- Validação `valida?` antiga, que nunca era chamada e esperava campos de outro projeto (`:valor`, `perdaCalorica`).
- Dependência `tools.cli`, que não era usada.
- Arquivos do template do Leiningen sem uso (`CHANGELOG.md` e `doc/intro.md` do cliente) e o teste que sempre falhava.

### Corrigido
- Consultar um cadastro existente no CLI lançava `NullPointerException`.
- O menu de cadastro misturava `read` e `read-line`, fazendo a leitura seguinte receber uma linha vazia.
- Mensagem "nEnviando dados..." sem a quebra de linha.

## [0.1.0] - 2026-09-21

### Adicionado
- API REST com cadastro de usuário, registro de ganho e perda calórica, extrato e saldo por período.
- Cliente de linha de comando para cadastro, lançamentos e consultas.
- Integração com APIs de tradução, tabela nutricional e gasto calórico por atividade.

# Sankhya App

Aplicação web moderna desenvolvida com React Router para gerenciamento de produtos e pedidos.

## Como Executar

### Pré-requisitos
- Node.js v22.15.1
- PNPM

### Passo a Passo

1. Clone o projeto
   - git clone [https://github.com/felipebs07/sankhya-app.git]
   - cd sankhya-app
   - altere o .env-exemplo para .env e configure corretamente.

2. Instale as dependências
   - pnpm install

3. Execute em modo desenvolvimento
   - pnpm run dev

4. Acesse a aplicação
    - Abra o navegador em: http://localhost:5173

5. Pronto! A aplicação está funcionando.


# Configuração do Ambiente
## Windows:
- Baixe e instale o Node.js v22.15.1 do site oficial
- Ou use o nvm: nvm install 22.15.1 && nvm use 22.15.1

## Linux:
### Ubuntu/Debian
- curl -fsSL https://deb.nodesource.com/setup_22.x | sudo -E bash -
- sudo apt-get install -y nodejs

## Mac
- brew install node@22

### Ou use nvm
- nvm install 22.15.1
- nvm use 22.15.1

### Instalar PNPM
- npm install -g pnpm

### Verificar Instalação
1. node -v  (Deve mostrar: v22.15.1)
2. pnpm -v  (Deve mostrar a versão do PNPM)

### Configurações
Você pode alterar as configurações no arquivo .env:
- PORT_BACKEND: Porta onde a aplicação do backend vai rodar (padrao é 8080)

## Comandos Disponíveis

- pnpm run dev        - Inicia servidor de desenvolvimento
- pnpm run build      - Gera build de produção
- pnpm run start      - Executa aplicação em produção
- pnpm run typecheck  - Verifica tipos TypeScript

## Tecnologias

- Node.js v22.15.1
- React Router 7.7.1
- React 19.1.0
- TypeScript 5.8.3
- TailwindCSS 4.1.4
- Vite 6.3.3
- PNPM (gerenciador de pacotes)

## Build para Produção

1. Gere o build:
   - pnpm build

2. Execute em produção:
   - pnpm start

## Aviso para possíveis problemas
- Se ocorrer algum erro com a porta do backend, altere a porta no arquivo .env
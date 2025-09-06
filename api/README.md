# API Sankhya

API desenvolvida com Spring Boot para gerenciamento de produtos e pedidos

## Como Executar

### Pré-requisitos
- Java 21
- Maven

### Passo a Passo

1. Clone o projeto
   git clone [url-do-repositorio]
   cd api

2. Configure as variáveis de ambiente
    - Copie o arquivo .env-exemplo para .env
    - cp .env-exemplo .env
    - Edite o arquivo .env com suas configurações (se necessário)

3. Execute a aplicação
   mvn spring-boot:run

4. Acesse a aplicação
    - Abra o navegador em: http://localhost:8080
    - Console H2: http://localhost:8080/h2-console

Pronto! A API está funcionando.

## Configuração do Ambiente

### Instalar Java 21

# Windows:
- Baixe e instale o JDK 21 da Oracle
- Configure JAVA_HOME nas variáveis de ambiente

# Linux/Mac:
### Ubuntu/Debian
sudo apt install openjdk-21-jdk

### Mac
brew install openjdk@21

### Verificar Instalação
1. java -version 
2. Deve mostrar: java 21.x.x

### Configurações
Você pode alterar as configurações no arquivo .env:
- SERVER_PORT: Porta onde a aplicação vai rodar
- DB_USERNAME e DB_PASSWORD: Credenciais do banco H2
- H2_CONSOLE_ENABLED: true para habilitar o console web do H2

## Tecnologias

- Java 21
- Spring Boot 3
- Spring Data JPA
- Spring MVC
- Lombok
- Jakarta EE
- Maven
- Banco H2 (em memória)

## Funcionalidades

- Products: CRUD com paginação
- Orders: Criação de pedidos com regras de negócio
- Checkout: Finalização de pedidos
- Console H2: Interface web para visualizar o banco

## Testes

Execute os testes:
mvn test

## Problemas Comuns

### Erro "arquivo .env não encontrado":
- Certifique-se de criar o arquivo .env baseado no .env-exemplo
- cp .env-exemplo .env

### Erro de versão Java:
1. Verifique se está usando Java 21
2. java -version

### Erro de dependências:
Limpe e recompile 
- mvn clean install

### Porta ocupada:
- Mude a SERVER_PORT no arquivo .env
- Ou mate o processo que está usando a porta

### Não consegue acessar H2 Console:
- Verifique se H2_CONSOLE_ENABLED=true no .env
- Acesse: http://localhost:8080/h2-console
- Use as credenciais do .env (usuário: sa, senha: password)

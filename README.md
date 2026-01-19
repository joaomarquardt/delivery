# Delivery - Arquitetura de microserviços

Este projeto é um ecossistema de Delivery baseado em uma arquitetura de microserviços, focado em aprender e aplicar comunicação assíncrona via **RabbitMQ**, roteamento via **API Gateway**, e segurança da aplicação com **Spring Security e JWT**.

A aplicação gerencia o ciclo de vida de um pedido, desde a criação até o processamento do pagamento e atualização de status.

---

## Arquitetura e Tecnologias

O sistema é dividido em 4 serviços principais:

1.  **Auth Service:** Gerencia usuários e autenticação JWT.
2.  **API Gateway:** Ponto de entrada único. Valida tokens e injeta headers de identificação (`X-User-Id`, `X-User-Role`) para os demais serviços.
3.  **Order Service:** Core do negócio. Gerencia produtos e o fluxo de estados dos pedidos.
4.  **Payment Service:** Processa pagamentos de forma assíncrona, simulando integração com gateways externos.

### Tech Stack:
- **Java / Spring Boot**
- **Spring Cloud Gateway** (Roteamento)
- **Spring Security & JWT** (Segurança)
- **RabbitMQ** (Mensageria)
- **MySQL** (Persistência)
- **Spring Data JPA** (Persistência)
- **Docker & Docker Compose** (Containerização)

---

## Principais Aplicações de Estudo

### API Gateway (Spring Cloud Gateway)
- Ponto de Entrada Único: Centraliza todas as chamadas da aplicação na porta 8080, simplificando o consumo para clientes externos e organizando o roteamento para os serviços de Auth, Order e Payment.
- Filtros de Requisição Customizados: Implementação de um UserHeaderFilter que atua na borda da aplicação, extraindo informações de identidade do JWT e injetando-as em Headers HTTP (X-User-Id, X-User-Role) para os serviços internos.
- Desacoplamento de Autenticação: Garante que os microserviços de negócio não precisem conhecer a lógica de decodificação do JWT, apenas confiar nos headers validados pelo Gateway.

### Mensageria e Resiliência (RabbitMQ)
- Comunicação Assíncrona: O Order Service publica eventos de novos pedidos, que são consumidos pelo Payment Service, permitindo que o sistema continue disponível mesmo sob alta carga.
- Dead Letter Exchange (DLX): Implementação de filas de erro (DLQ) para tratar mensagens que falham no processamento (ex: inconsistência de dados), evitando perda de informações e loops infinitos de erro (Poison Messages).
- Idempotência: Consumidores preparados para verificar o estado atual do banco antes de processar mensagens, evitando efeitos colaterais em caso de reprocessamento.

### Segurança e Contexto
- Stateless Architecture: Uso de tokens JWT para manter a sessão do usuário de forma escalável e sem estado nos servidores.
- Role-Based Access Control (RBAC): Segurança baseada em papéis (ADMIN, CUSTOMER, DELIVERY_DRIVER), aplicada tanto em nível de rota quanto em nível de método.
- Validation & Exceptions: Tratamento de erros personalizado com GlobalExceptionHandler e Exceções de Domínio customizadas, garantindo que a API retorne status HTTP semânticos (ex: 400, 401, 403, 409) e mensagens de erro claras.

---

## Rotas de Acesso (Via Gateway: 8080)

Como o sistema utiliza um API Gateway, todas as requisições devem ser feitas para a porta `8080`. O Gateway fará o roteamento baseado no prefixo da URL:

| Prefixo da Rota | Microserviço Destino | Responsabilidade |
| :--- | :--- | :--- |
| `/api/auth/**` | **Auth Service** | Login, Registro e Validação |
| `/api/orders/**` | **Order Service** | Gestão de Pedidos e Produtos |
| `/api/payments/**` | **Payment Service** | Processamento de Pagamentos |

---

## Diagrama da Aplicação
<img width="1032" height="750" alt="image" src="https://github.com/user-attachments/assets/90c310f6-2741-4e1a-8a49-fb813ba36c86" />

---

## Como Rodar a Aplicação

Existem duas formas de executar o projeto:

### 1. Execução Total via Docker (Recomendado)
Esta forma sobe todos os microserviços, bancos de dados e o broker de mensagens de uma vez. Para isso é necessário ter o Docker instalado.

1.1. Na raiz do projeto, altere o nome do arquivo `.env-template` para `.env` e preencha o conteúdo dentro dele para que o Docker possa estabelecer conexão com seu banco:
```env
MYSQL_USER=seu_usuario
MYSQL_PASSWORD=sua_senha
```

1.2. Rode os comandos abaixo no terminal:

```bash
# Clone o repositório
git clone https://github.com/joaomarquardt/delivery.git

# Entre na pasta raiz
cd delivery

# Suba todos os containers
docker-compose up -d
```

### 2. Execução Separada (Desenvolvimento/IDE)
Abordagem para debug ou rápidas alterações no código. Nessa opção somente o RabbitMQ é buildado no Docker.

2.1. Rode os comandos abaixo no terminal:
```bash
# Clone o repositório
git clone https://github.com/joaomarquardt/delivery.git

# Entre na pasta raiz
cd delivery

# Suba apenas o RabbitMQ
docker-compose up rabbitmq -d
```
2.2 Na sua IDE (IntelliJ/Eclipse), crie uma configuração de execução e adicione os VM Options para usar o perfil de desenvolvimento: `-Dspring.profiles.active=dev`.
  
2.3. Vá em Edit Run Configurations e, no campo "Environment Variables", adicione suas credenciais do banco MySQL: `MYSQL_USER=seu_usuario;MYSQL_PASSWORD=sua_senha`.

2.4. Execute o main de cada serviço (Auth, Order, Payment, Gateway).

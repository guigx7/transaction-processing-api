# Transaction Processing API

## Visão Geral

Este projeto é um **microserviço backend em Java (Spring Boot)** que simula o **processamento de ordens de pagamento**, utilizando **Oracle Database** como banco de dados e **PL/SQL** para regras críticas de negócio.

O objetivo principal é demonstrar, de forma prática e corporativa, um cenário comum em **sistemas de back office bancário**, cobrindo:

- Desenvolvimento em Java
- API REST
- Integração Java ↔ Oracle
- Uso de PL/SQL (packages e procedures)
- Controle transacional no banco
- Integração REST entre sistemas
- Arquitetura em camadas / microserviço

O banco Oracle é provisionado localmente via **Docker**.

---

## Pitch rápido do projeto

> A API recebe uma ordem de pagamento, registra a transação no Oracle com status **PENDING** via PL/SQL, chama um sistema externo para processamento e, conforme a resposta, atualiza o status final da transação para **PROCESSED** ou **FAILED**.

---

## Arquitetura Geral

Fluxo principal do sistema:

```
Client (Postman / outro sistema)
   ↓
Controller REST (Spring Boot)
   ↓
Service (orquestração)
   ↓
Repository (JDBC / PL-SQL)
   ↓
Oracle Database
   ↓
Serviço REST Externo (mock)
   ↓
Atualização de status no Oracle
```

### Decisão arquitetural

As **regras críticas de persistência e transação** ficam no **banco de dados (PL/SQL)**, enquanto o Java atua como **orquestrador do fluxo**. Esse padrão é muito comum em ambientes corporativos e bancários.

---

## Estrutura do Projeto

```
transaction-processing-api
├── src/main/java/com/example/transactionprocessing
│   ├── controller        # Controllers REST
│   ├── service           # Orquestração do fluxo de negócio
│   ├── repository        # Integração Java ↔ PL/SQL
│   ├── client            # Integrações REST externas
│   ├── dto               # Objetos de transporte de dados
│   ├── model             # Modelo de domínio
│   └── enums             # Enums do domínio
│
├── db
│   ├── ddl               # Scripts de criação de tabelas e sequences
│   └── plsql             # Packages e procedures PL/SQL
│
├── docker
│   └── oracle            # Docker Compose do Oracle Database
│
└── README.md
```

---

## Banco de Dados (Oracle)

### Provisionamento

O banco de dados é executado localmente via Docker utilizando a imagem gratuita:

- `gvenzl/oracle-free`

### Subir o banco

```bash
cd docker/oracle
docker compose up -d
```

### Conexão

- Host: `localhost`
- Porta: `1521`
- Service Name: `XEPDB1`
- Usuário da aplicação: `txn_user`

---

## Modelagem de Dados

### Tabela `TRANSACTIONS`

Tabela responsável por armazenar as ordens de pagamento.

Campos principais:

- `ID` – Identificador da transação
- `ACCOUNT_FROM` – Conta de origem
- `ACCOUNT_TO` – Conta de destino
- `AMOUNT` – Valor da transação
- `STATUS` – Status do processamento (`PENDING`, `PROCESSED`, `FAILED`)
- `CREATED_AT` – Data de criação

A integridade do status é garantida por **constraint no banco**.

---

## PL/SQL

### Package `PKG_TRANSACTION`

Centraliza as regras críticas no banco de dados.

Procedures principais:

- `CREATE_TRANSACTION`
  - Insere a transação
  - Define status inicial como `PENDING`
  - Controla `COMMIT` / `ROLLBACK`
  - Retorna o ID gerado

- `UPDATE_TRANSACTION_STATUS`
  - Atualiza o status da transação
  - Controla transação

Os scripts estão versionados em:

```
db/plsql
```

Mesmo sendo executados manualmente no banco, permanecem no repositório para **versionamento, auditoria e reprodutibilidade**.

---

## Backend Java (Spring Boot)

### Controllers

#### TransactionController

Endpoint principal do sistema:

```
POST /transactions
```

Responsável por receber a requisição e disparar o fluxo completo de processamento.

#### ExternalProcessorController

Simula um **sistema externo** que aprova ou rejeita a transação.

```
POST /external-processor/process
```

---

### Service Layer

#### TransactionService

Responsável por **orquestrar o fluxo**:

1. Criar transação no Oracle via PL/SQL
2. Chamar o serviço externo REST
3. Atualizar o status no banco conforme o resultado

---

### Repository Layer

#### TransactionRepository

Responsável por chamar **procedures PL/SQL** usando `SimpleJdbcCall`.

Características importantes:

- Uso de `spring-jdbc`
- Parâmetros declarados explicitamente
- Metadata lookup desabilitado
- Schema e package definidos manualmente

Esse padrão evita falhas silenciosas comuns na integração com Oracle.

---

### Integração REST Externa

A integração com sistemas externos é isolada na camada de **client**, representando cenários reais como:

- sistemas legados
- processadores bancários
- parceiros externos

---

## Como Rodar o Projeto

### Pré-requisitos

- Java 17
- Maven
- Docker
- Oracle SQL Developer (opcional)

### Passos

1. Subir o Oracle via Docker

```bash
cd docker/oracle
docker compose up -d
```

2. Executar os scripts de banco (DDL e PL/SQL)

Localizados em:

```
db/ddl
db/plsql
```

3. Subir a aplicação Spring Boot

```bash
mvn spring-boot:run
```

4. Testar com Postman

```
POST http://localhost:8080/transactions
```

```json
{
  "accountFrom": "ACC100",
  "accountTo": "ACC200",
  "amount": 300.50
}
```

---

## Resultado Esperado

- A API retorna a transação criada
- O status final pode ser `PROCESSED` ou `FAILED`
- O registro fica persistido no Oracle

---


## Considerações Finais

O projeto está estruturado para fácil evolução, permitindo inclusão de:

- autenticação
- processamento assíncrono
- mensageria
- auditoria avançada


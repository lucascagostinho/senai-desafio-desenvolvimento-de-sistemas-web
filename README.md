# API de Destinos de Viagem — Agência de Viagens

API RESTful para gerenciamento de destinos de viagem, desenvolvida como desafio prático da disciplina de Desenvolvimento de Sistemas Web (SENAI/SC — Análise e Desenvolvimento de Sistemas).

Esta versão evolui a entrega anterior introduzindo persistência real com PostgreSQL via Spring Data JPA, autenticação e autorização com Spring Security (HTTP Basic Auth), controle de acesso por perfil de usuário e separação entre DTOs de entrada e saída.

---

## 1. Visão Geral do Problema

A agência de viagens deseja modernizar seus serviços digitais expondo uma API REST que permita a integração com aplicativos de turismo, parceiros comerciais e futuras plataformas digitais.

A primeira entrega estabeleceu a arquitetura da solução e os endpoints de gerenciamento de destinos. Após a entrega, a equipe técnica identificou a necessidade de evoluir a aplicação para um ambiente mais próximo da produção — os dados ainda eram mantidos temporariamente em memória e não havia controle de acesso aos recursos da API.

Em sistemas profissionais, essas limitações precisam ser superadas. Uma API usada por parceiros, aplicativos externos e equipes internas deve persistir dados em banco, controlar quem pode acessar cada funcionalidade e proteger operações sensíveis. Por isso, a agência decidiu avançar para a próxima etapa do projeto.

A API permite: cadastrar, listar, pesquisar por nome ou por localização, detalhar, atualizar, avaliar e excluir destinos de viagem, com dados persistidos em banco de dados e acesso protegido por autenticação e perfil de usuário.

---

## 2. Arquitetura

O projeto segue uma arquitetura em camadas dentro de uma única aplicação Spring Boot.

**Responsabilidade de cada camada:**

- **Controller**: único ponto de contato com o mundo externo via HTTP. Não contém regra de negócio — apenas recebe a requisição, delega para o `Service` e devolve a resposta com o status HTTP adequado.
- **Service**: concentra toda a lógica de negócio da aplicação, incluindo o cálculo da média de avaliação de um destino e a conversão entre entidades e DTOs.
- **Repository**: responsável pelo acesso ao banco de dados via Spring Data JPA. Contém as queries customizadas de busca e agregação.
- **Entity**: representa as tabelas do banco de dados com o mapeamento ORM via Hibernate.
- **DTO**: objetos de transferência de dados com separação entre entrada (`RequestDTO`) e saída (`DTO`). Desacopla o contrato da API do modelo de banco.
- **Config**: configuração de segurança (Spring Security), definindo regras de acesso por endpoint e perfil de usuário.

Essa separação garante que mudanças em uma camada não impactem as demais. Se o banco de dados mudar, apenas a camada `Repository` e as `Entities` precisam ser ajustadas — `Controller` e `Service` permanecem inalterados.

### Estrutura de pacotes

```
com.travelagency.destinations
├── config/
│   └── SecurityConfig.java
├── controllers/
│   ├── destination/DestinationController.java
│   ├── review/ReviewController.java
│   └── user/UserController.java
├── dtos/
│   ├── destination/DestinationDTO.java          
│   ├── destination/DestinationRequestDTO.java   
│   ├── review/ReviewDTO.java                    
│   ├── review/ReviewRequestDTO.java             
│   └── user/UserRequestDTO.java                 
├── entities/
│   ├── destination/DestinationEntity.java
│   ├── review/ReviewEntity.java
│   └── user/UserEntity.java
├── repositories/
│   ├── destination/DestinationRepository.java
│   ├── review/ReviewRepository.java
│   └── user/UserRepository.java
└── services/
    ├── destination/DestinationService.java
    ├── review/ReviewService.java
    └── user/UserService.java
```

---

## 3. Justificativa de Linguagem, Framework e Tecnologias

- **Java 21**: versão LTS (Long-Term Support) com suporte estendido, recomendada para projetos novos no mercado. Mantém consistência com o restante do curso e com o padrão adotado pela maioria das empresas brasileiras em novos projetos backend.

- **Spring Boot 4**: reduz drasticamente a complexidade de configuração de uma API REST (servidor embutido, injeção de dependência automática, serialização JSON), permitindo focar na modelagem do problema em vez de infraestrutura. É o framework mais utilizado no mercado brasileiro para APIs Java.

- **Spring Web MVC**: camada HTTP do Spring, usada para definir os controllers REST e mapear as rotas da API.

- **Spring Data JPA**: abstração sobre o Hibernate que permite escrever repositórios como interfaces, sem SQL manual para operações comuns. Buscas customizadas são expressas por convenção de nome de método (`findAllByNameContainingIgnoreCase`) ou por anotação `@Query` para agregações como a média de avaliações.

- **Spring Security**: framework de segurança padrão do ecossistema Spring. Nesta versão, utiliza **HTTP Basic Authentication** — o cliente envia usuário e senha codificados em Base64 a cada requisição. A autorização por perfil (`ADMIN`, `USER`) é configurada via `SecurityFilterChain`.

- **PostgreSQL**: banco de dados relacional robusto, open source e amplamente utilizado em produção. Escolhido por compatibilidade com Spring Data JPA e por ser o banco configurado no ambiente do desafio.

- **BCrypt**: algoritmo de hash de senhas intencionalmente lento, resistente a ataques de força bruta. Usado via `BCryptPasswordEncoder` do Spring Security — senhas nunca são armazenadas em texto puro.

- **Maven**: ferramenta de build e gerenciamento de dependências padrão no ecossistema Spring Boot.

---

## 4. Segurança

A API usa **HTTP Basic Authentication**. O cliente envia `usuario:senha` codificados em Base64 no header `Authorization` a cada requisição protegida.

### Regras de acesso por endpoint

| Endpoint                         | Acesso                       |
| -------------------------------- | ---------------------------- |
| `POST /users/register`           | Público (sem autenticação)   |
| `GET /destinations/**`           | Público (sem autenticação)   |
| `POST /destinations`             | Somente role `ADMIN`         |
| `PUT /destinations/{id}`         | Somente role `ADMIN`         |
| `DELETE /destinations/{id}`      | Somente role `ADMIN`         |
| `/reviews/**` (todos os métodos) | Qualquer usuário autenticado |

### Roles disponíveis

| Role    | Descrição                                            |
| ------- | ---------------------------------------------------- |
| `ADMIN` | Acesso total — pode criar, editar e excluir destinos |
| `USER`  | Acesso às reviews e leitura de destinos              |

> **Nota:** o campo `role` é enviado em maiúsculas (`ADMIN`, `USER`). O Spring Security adiciona o prefixo `ROLE_` internamente.

---

## 5. Pré-requisitos

- **Java 21** — `java -version`
- **Maven** — `mvn -version`
- **PostgreSQL** rodando localmente na porta `5432`

### Configuração do banco

Crie o banco antes de subir a aplicação:

```sql
CREATE DATABASE travel_agency;
```

As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`) na primeira execução.

### Configuração da aplicação

O arquivo `src/main/resources/application.properties` contém:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/travel_agency
spring.datasource.username=postgres
spring.datasource.password=admin
```

Ajuste `username` e `password` conforme a sua instalação local do PostgreSQL.

---

## 6. Como executar

```bash
# 1. Clonar o repositório
git clone <url-do-repositorio>
cd <pasta-do-projeto>/destinations

# 2. Executar a aplicação
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Testando a API

Recomenda-se o uso do **Postman** ou **Insomnia** para testar os endpoints. Basta importar as requisições de exemplo desta documentação (seção 8) apontando para `http://localhost:8080`.

Diferente da versão anterior, os dados agora são persistidos no banco PostgreSQL e **não são perdidos** ao reiniciar a aplicação.

---

## 7. Como testar com Postman

### Configurando autenticação no Postman

1. Abra a requisição no Postman
2. Aba **Authorization** → Type: **Basic Auth**
3. Preencha **Username** e **Password**
4. O Postman monta o header `Authorization: Basic <base64>` automaticamente

### Fluxo recomendado para testar

**Passo 1 — Registrar um usuário ADMIN:**

```
POST http://localhost:8080/users/register
(sem autenticação)
```

```json
{
  "username": "admin",
  "password": "123456",
  "role": "ADMIN"
}
```

**Passo 2 — Registrar um usuário comum:**

```
POST http://localhost:8080/users/register
(sem autenticação)
```

```json
{
  "username": "usuario",
  "password": "123456",
  "role": "USER"
}
```

**Passo 3 — Criar um destino (requer ADMIN):**

```
POST http://localhost:8080/destinations
Authorization: Basic Auth → admin / 123456
```

```json
{
  "name": "Foz do Iguaçu",
  "location": "Paraná, Brasil",
  "description": "Cataratas do Iguaçu e Parque Nacional"
}
```

**Passo 4 — Listar destinos (público):**

```
GET http://localhost:8080/destinations
(sem autenticação)
```

**Passo 5 — Criar uma review (requer autenticação):**

```
POST http://localhost:8080/reviews
Authorization: Basic Auth → usuario / 123456
```

```json
{
  "rating": 5,
  "destinationId": 1
}
```

**Passo 6 — Ver o destino com o rating calculado:**

```
GET http://localhost:8080/destinations/1
(sem autenticação)
```

---

## 8. Endpoints da API

Base URL: `http://localhost:8080`

---

### 8.1. Usuários

#### Registrar usuário

`POST /users/register` — **Público**

**Corpo da requisição:**

```json
{
  "username": "admin",
  "password": "123456",
  "role": "ADMIN"
}
```

**Resposta — `201 Created`** _(sem corpo)_

---

### 8.2. Destinos

#### Criar destino

`POST /destinations` — **Requer role ADMIN**

**Corpo da requisição:**

```json
{
  "name": "Foz do Iguaçu",
  "location": "Paraná, Brasil",
  "description": "Cataratas do Iguaçu e Parque Nacional"
}
```

**Resposta — `201 Created`:**

```json
{
  "id": 1,
  "name": "Foz do Iguaçu",
  "location": "Paraná, Brasil",
  "description": "Cataratas do Iguaçu e Parque Nacional",
  "rating": null
}
```

> `rating` é `null` quando o destino ainda não possui nenhuma avaliação.

---

#### Listar todos os destinos

`GET /destinations` — **Público**

**Resposta — `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Foz do Iguaçu",
    "location": "Paraná, Brasil",
    "description": "Cataratas do Iguaçu e Parque Nacional",
    "rating": 4.5
  },
  {
    "id": 2,
    "name": "Bonito",
    "location": "Mato Grosso do Sul, Brasil",
    "description": "Ecoturismo e rios de águas cristalinas",
    "rating": null
  }
]
```

---

#### Buscar destino por ID

`GET /destinations/{id}` — **Público**

**Resposta — `200 OK`:**

```json
{
  "id": 1,
  "name": "Foz do Iguaçu",
  "location": "Paraná, Brasil",
  "description": "Cataratas do Iguaçu e Parque Nacional",
  "rating": 4.5
}
```

---

#### Buscar destinos por nome

`GET /destinations/search/name?name={termo}` — **Público**

Busca parcial, sem distinção de maiúsculas/minúsculas.

**Exemplo:** `GET /destinations/search/name?name=foz`

**Resposta — `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Foz do Iguaçu",
    "location": "Paraná, Brasil",
    "description": "Cataratas do Iguaçu e Parque Nacional",
    "rating": 4.5
  }
]
```

---

#### Buscar destinos por localização

`GET /destinations/search/location?location={termo}` — **Público**

Busca parcial, sem distinção de maiúsculas/minúsculas.

**Exemplo:** `GET /destinations/search/location?location=paraná`

**Resposta — `200 OK`:**

```json
[
  {
    "id": 1,
    "name": "Foz do Iguaçu",
    "location": "Paraná, Brasil",
    "description": "Cataratas do Iguaçu e Parque Nacional",
    "rating": 4.5
  }
]
```

---

#### Atualizar destino

`PUT /destinations/{id}` — **Requer role ADMIN**

**Corpo da requisição:**

```json
{
  "name": "Foz do Iguaçu",
  "location": "Paraná, Brasil",
  "description": "Cataratas do Iguaçu, Parque Nacional e Marco das Três Fronteiras"
}
```

**Resposta — `200 OK`:**

```json
{
  "id": 1,
  "name": "Foz do Iguaçu",
  "location": "Paraná, Brasil",
  "description": "Cataratas do Iguaçu, Parque Nacional e Marco das Três Fronteiras",
  "rating": 4.5
}
```

---

#### Excluir destino

`DELETE /destinations/{id}` — **Requer role ADMIN**

**Resposta — `204 No Content`** _(sem corpo)_

---

### 8.3. Reviews (Avaliações)

#### Criar review

`POST /reviews` — **Requer autenticação**

**Corpo da requisição:**

```json
{
  "rating": 5,
  "destinationId": 1
}
```

**Resposta — `201 Created`:**

```json
{
  "id": 1,
  "rating": 5,
  "destinationId": 1
}
```

---

#### Listar todas as reviews

`GET /reviews` — **Requer autenticação**

**Resposta — `200 OK`:**

```json
[
  {
    "id": 1,
    "rating": 5,
    "destinationId": 1
  },
  {
    "id": 2,
    "rating": 4,
    "destinationId": 1
  }
]
```

---

#### Buscar review por ID

`GET /reviews/{id}` — **Requer autenticação**

**Resposta — `200 OK`:**

```json
{
  "id": 1,
  "rating": 5,
  "destinationId": 1
}
```

---

#### Buscar reviews por destino

`GET /reviews/search/destination?id={destinationId}` — **Requer autenticação**

**Exemplo:** `GET /reviews/search/destination?id=1`

**Resposta — `200 OK`:**

```json
[
  {
    "id": 1,
    "rating": 5,
    "destinationId": 1
  },
  {
    "id": 2,
    "rating": 4,
    "destinationId": 1
  }
]
```

---

#### Atualizar review

`PUT /reviews/{id}` — **Requer autenticação**

**Corpo da requisição:**

```json
{
  "rating": 4,
  "destinationId": 1
}
```

**Resposta — `200 OK`:**

```json
{
  "id": 1,
  "rating": 4,
  "destinationId": 1
}
```

---

#### Excluir review

`DELETE /reviews/{id}` — **Requer autenticação**

**Resposta — `204 No Content`** _(sem corpo)_

---

## 9. Resumo dos endpoints

| Método   | Endpoint                                  | Ação                       | Acesso      |
| -------- | ----------------------------------------- | -------------------------- | ----------- |
| `POST`   | `/users/register`                         | Registrar usuário          | Público     |
| `POST`   | `/destinations`                           | Criar destino              | ADMIN       |
| `GET`    | `/destinations`                           | Listar destinos            | Público     |
| `GET`    | `/destinations/{id}`                      | Buscar por ID              | Público     |
| `GET`    | `/destinations/search/name?name=`         | Buscar por nome            | Público     |
| `GET`    | `/destinations/search/location?location=` | Buscar por localização     | Público     |
| `PUT`    | `/destinations/{id}`                      | Atualizar destino          | ADMIN       |
| `DELETE` | `/destinations/{id}`                      | Excluir destino            | ADMIN       |
| `POST`   | `/reviews`                                | Criar review               | Autenticado |
| `GET`    | `/reviews`                                | Listar reviews             | Autenticado |
| `GET`    | `/reviews/{id}`                           | Buscar review por ID       | Autenticado |
| `GET`    | `/reviews/search/destination?id=`         | Buscar reviews por destino | Autenticado |
| `PUT`    | `/reviews/{id}`                           | Atualizar review           | Autenticado |
| `DELETE` | `/reviews/{id}`                           | Excluir review             | Autenticado |

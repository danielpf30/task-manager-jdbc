#  To-Do List API – Java 21 + Spring Boot

API RESTful para gerenciamento de tarefas desenvolvida com Java 21 e Spring Boot 3, projetada com foco em arquitetura limpa, validação rigorosa e estratégia sólida de testes.

##  Visão Geral
A API gerencia o ciclo completo de vida de tarefas diárias, oferecendo:

* ✔ **CRUD completo** (POST, GET, PUT, PATCH, DELETE)
* ✔ **Atualização parcial** com PATCH
* ✔ **Validação automática** de dados com retorno HTTP 400
* ✔ **Status inteligente** com valor padrão (PENDENTE)
* ✔ **Persistência** com controle total das queries SQL

##  Arquitetura
A aplicação segue uma Arquitetura em Camadas, garantindo separação clara de responsabilidades:

`Controller` → `Service` → `Repository` → `PostgreSQL`



###  Proteção na Borda (DTO)
Toda comunicação externa ocorre via DTOs. A validação é realizada com **Jakarta Validation**, e as Entidades de domínio não são expostas diretamente. Isso garante:
*  Contrato de API limpo
*  Validação na entrada da aplicação
*  Desacoplamento entre transporte e domínio

###  Estratégia de Persistência
Ao invés de utilizar ORM (como JPA/Hibernate), o projeto utiliza:
* Spring `JdbcTemplate`
* Queries SQL manuais
* Controle explícito da comunicação com o banco
* Banco de dados: **PostgreSQL** (Dockerizado)

##  Estratégia de Testes – Pirâmide Aplicada
A estabilidade da aplicação é garantida por uma estrutura clara de testes automatizados:

🔹 **Controller – Testes Fatiados**
* **Ferramenta:** `@WebMvcTest`
* **Foco:** Rotas, Status HTTP e conversão JSON/DTO.
* **Segurança:** Validação de entrada com `@Valid`.
* **Performance:** Banco de dados isolado (rodando em milissegundos).

🔹 **Service – Testes Unitários**
* **Ferramentas:** JUnit 5 + Mockito
* **Foco:** Regras de negócio e definição de status padrão.
* **Comportamento:** Lógica de atualização parcial (PATCH).
* **Isolamento:** Camada de banco de dados 100% mockada.

🔹 **Repository – Testes de Integração**
* **Ferramentas:** Spring Boot Test + Testcontainers (Docker)
* **Foco:** Execução real e exata das queries SQL.
* **Mapeamento:** Conversão de `ResultSet` para objetos Java.
* **Ambiente:** Testado diretamente contra o PostgreSQL real.
  
##  Infraestrutura com Docker
O PostgreSQL é executado em container Docker, mapeado para a porta externa **5433**, evitando conflitos com instalações locais.

##  Tecnologias Utilizadas
* Java 21
* Spring Boot 3 (Web)
* **Spring JdbcTemplate** (Persistência de dados com controle nativo de SQL)
* **Jakarta Validation** (Blindagem e validação de DTOs na borda da API)
* PostgreSQL
* Docker
* JUnit 5 & Mockito
* Lombok & Jackson

---

##  Como Executar o Projeto

### 🔹 Pré-requisitos
* Java 21 ou superior
* Maven
* Docker

### 1️⃣ Clonar o Repositório
```bash
git clone https://github.com/danielpf30/task-manager-jdbc.git

2️⃣ Subir o Banco de Dados (Docker)
Bash
docker compose up -d
3️⃣ Verificar application.properties
Properties
spring.datasource.url=jdbc:postgresql://localhost:5433/db_tasks
spring.datasource.username=admin
spring.datasource.password=admin
4️⃣ Iniciar a Aplicação
Bash
mvn spring-boot:run
A API estará disponível em: http://localhost:8080/tasks

5️⃣ Executar os Testes
Bash
mvn test
🔎 Exemplos de Uso da API
➕ Criar Tarefa (POST)
POST http://localhost:8080/tasks

JSON
{
  "description": "Testando o projeto",
  "status": "Em Progresso",
  "dateLimit": "2026-03-02"
}
(Caso a descrição esteja vazia ou a data seja anterior ao dia atual: HTTP 400 - Bad Request)

✏ Atualização Parcial (PATCH)
PATCH http://localhost:8080/tasks/1

JSON
{
  "status": "Concluída"
}
(Apenas os campos enviados são alterados. Os demais permanecem inalterados no banco de dados).

 Objetivo do Projeto
O foco deste projeto não foi apenas “fazer funcionar”, mas construir uma API:
 Sustentável | Testável | Performática | Segura | Arquiteturalmente organizada

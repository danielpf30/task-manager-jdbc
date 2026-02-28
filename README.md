##  Visão Geral

###  O que é?
Uma API RESTful para gerenciamento de tarefas (To-Do List), desenvolvida em Java com Spring Boot para servir como o back-end (motor) de aplicações web ou mobile.

###  O que faz?
Gerencia o ciclo de vida completo de uma tarefa diária. Suas principais funções incluem:
* **CRUD Completo:** Criar, listar, atualizar (total via `PUT` ou parcial via `PATCH`) e deletar tarefas.
* **Validação de Dados:** Bloqueia automaticamente requisições inválidas (ex: datas no passado ou descrições vazias) devolvendo mensagens claras (Erro 400).
* **Gestão de Status:** Processa o status da tarefa de forma inteligente (Pendente, Em Progresso, Concluída), assumindo "Pendente" por padrão quando omitido.

###  Como faz?
A aplicação utiliza uma **Arquitetura em Camadas** (Controller, Service, Repository) focada em controle e performance:
* **Proteção na Borda:** A internet não toca no banco de dados. A comunicação é feita apenas com **DTOs** (Data Transfer Objects), que são blindados pelo `Jakarta Validation` logo na entrada da API.
* **Persistência com JdbcTemplate:** Diferente de projetos acoplados a ORMs (como JPA/Hibernate), a comunicação com o **PostgreSQL** é feita de forma manual e otimizada usando o Spring `JdbcTemplate`, garantindo controle absoluto sobre as queries SQL.

* ##  Tecnologias Utilizadas

* **Back-end:** Java (JDK 21), Spring Boot 3 (Web, Validation, JDBC).
* **Banco de Dados:** PostgreSQL.
* **Infraestrutura:** Docker (Containerização do banco de dados).
* **Testes:** JUnit 5, Mockito, Spring Boot Test.
* **Utilitários:** Lombok (Redução de boilerplate corporativo), Jackson (Serialização inteligente de JSON).

##  Estratégia de Testes

A estabilidade da aplicação foi garantida aplicando a pirâmide de testes, separando as responsabilidades para que os testes rodem em milissegundos:

* **Testes Fatiados (Sliced Tests):** Utilização da anotação `@WebMvcTest` nos Controllers. Isso permite testar exclusivamente a camada de internet (rotas, validações do `@Valid`, conversão de JSON para DTO e Status HTTP) isolada do banco de dados, tornando os testes extremamente rápidos.
* **Testes Unitários:** Focados na camada de `Service` (Regras de Negócio). O comportamento da lógica (como o "bisturi" do método PATCH) é validado utilizando o **Mockito** para criar "dublês" (Mocks) do banco de dados.
* **Testes de Integração (Repository):** Focados na camada de persistência. Como a aplicação utiliza `JdbcTemplate`, esses testes validam a execução correta das queries SQL manuais e o mapeamento dos dados extraídos do banco de dados para os objetos Java, garantindo a integridade total da comunicação com o PostgreSQL.

##  Infraestrutura com Docker

Para garantir que o projeto rode em qualquer máquina sem "dores de cabeça" com instalações locais, o banco de dados PostgreSQL foi isolado utilizando **Docker**. O container deste projeto foi mapeado para a porta externa **5433**, evitando qualquer colisão de portas.

##  Como Executar e Testar a Aplicação

### Pré-requisitos
Antes de começar, certifique-se de ter instalado em sua máquina:
* **Java JDK 21** (ou superior)
* **Maven**
* **Docker** (Para rodar o banco de dados sem precisar instalar o PostgreSQL localmente)

### Passo 1: Clonar o Repositório
Abra o seu terminal e baixe o código fonte do projeto:
```bash
git clone [https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO.git](https://github.com/SEU_USUARIO/NOME_DO_REPOSITORIO.git)
cd NOME_DO_REPOSITORIO

Passo 2: Subir o Banco de Dados (Docker)
Para evitar conflitos com instalações locais do PostgreSQL (que geralmente ocupam a porta 5432), o container deste projeto foi mapeado para a porta externa 5433. Execute o comando abaixo no terminal para instanciar o banco:

Bash
docker compose up -d

Passo 3: Configurar o Spring Boot
Verifique se o arquivo application.properties está apontando corretamente para o container que acabou de criar. As configurações devem estar assim:

Properties
spring.application.name=To-Do-List
spring.datasource.url=jdbc:postgresql://localhost:5433/db_tasks
spring.datasource.username=admin
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

Passo 4: Iniciar a API
Com o banco de dados rodando, você pode iniciar o servidor do Spring Boot. Na raiz do projeto.

Passo 5: Testar as Rotas (Postman / Insomnia)
Com a API no ar, você pode realizar requisições para a URL base http://localhost:8080/tasks.

Exemplo 1: Criando uma tarefa (POST)

JSON
// POST http://localhost:8080/tasks
{
  "description": "Testing the project",
  "status": "Em Progresso",
  "dateLimit": "2026-02-28"
}
(O sistema possui validações ativas. Se enviar uma data no passado ou omitir a descrição, a API retornará o Status 400 Bad Request).

Exemplo 2: Atualização Parcial com (PATCH)

JSON
// PATCH http://localhost:8080/tasks/1
{
  "status": "Concluída"
}
(Neste exemplo, apenas o status da tarefa de ID 1 será alterado. A descrição original e a data limite permanecerão intactas no banco de dados).

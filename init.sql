-- create table tasks if don´t exists
CREATE TABLE IF NOT EXISTS tarefas (
  id BIGSERIAL PRIMARY KEY,
  descricao VARCHAR(255) NOT NULL,
  status VARCHAR(20) DEFAULT 'PENDENTE',
  data_limite DATE
  );
-- add a test task so we can validate the connect later
-- Insere uma tarefa de teste para validarmos a conexão depois
INSERT INTO tarefas (descricao, status, data_limite)
VALUES ('Configurar o banco de dados com Docker', 'CONCLUIDA', CURRENT_DATE);
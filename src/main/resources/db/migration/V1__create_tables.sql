CREATE TABLE papel (
                       id SERIAL PRIMARY KEY,
                       nome VARCHAR(50) NOT NULL
);

CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(100) NOT NULL,
                         papel_id INTEGER REFERENCES papel(id),
                         avatar_path VARCHAR(255)
);

CREATE TABLE disciplina (
                            id SERIAL PRIMARY KEY,
                            nome VARCHAR(100) NOT NULL,
                            codigo VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE monitoria (
                           id SERIAL PRIMARY KEY,
                           status VARCHAR(255) NOT NULL CHECK (status IN ('ENCERRADA', 'ABERTA', 'ATIVA')),
                           horario VARCHAR(50),
                           ano INT NOT NULL,
                           semestre INT NOT NULL,
                           descricao TEXT,
                           professor_id INTEGER REFERENCES usuario(id),
                           monitor_id INTEGER REFERENCES usuario(id),
                           disciplina_id INTEGER REFERENCES disciplina(id)
);

CREATE TABLE presenca (
                          id SERIAL PRIMARY KEY,
                          nr_alunos INT NOT NULL,
                          data DATE NOT NULL,
                          monitoria_id INTEGER REFERENCES monitoria(id)
);

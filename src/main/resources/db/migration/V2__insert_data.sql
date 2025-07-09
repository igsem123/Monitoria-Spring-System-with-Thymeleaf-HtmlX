-- PAPÉIS
INSERT INTO papel (nome)
VALUES ('Professor'),
       ('Monitor'),
       ('Administrador');

-- USUÁRIOS
INSERT INTO usuario (nome, email, senha, papel_id)
VALUES ('Ana Professor', 'ana.professor@email.com', '$2a$12$6riP6as/r0oeUZNeF.jZuO8GAHuYRJ/1w9Y9.XSlNbnsBRgdoOUEW', 1),
       ('Carlos Monitor', 'carlos.monitor@email.com', '$2a$12$6riP6as/r0oeUZNeF.jZuO8GAHuYRJ/1w9Y9.XSlNbnsBRgdoOUEW', 2),
       ('Larissa Admin', 'larissa@email.com', '$2a$12$6riP6as/r0oeUZNeF.jZuO8GAHuYRJ/1w9Y9.XSlNbnsBRgdoOUEW', 3),
       ('Raphael Admin', 'rnathanmoreira@gmail.com', '$2a$12$/A5xyD.h/3XLfjLD39T2MePELaS5Od5vm/ZEmoPnSJMQzveMRlkPK', 3);

-- DISCIPLINAS
INSERT INTO disciplina (nome, codigo)
VALUES ('Estrutura de Dados', 'ED123'),
       ('Programação Orientada a Objetos', 'POO456');

-- MONITORIAS
INSERT INTO monitoria (status, horario, ano, semestre, descricao, professor_id, monitor_id, disciplina_id)
VALUES ('ATIVA', 'Segundas 10h', 2025, 1, null, 1, 2, 1),
       ('ABERTA', 'Quartas 14h', 2024, 2, null, 1, null, 2);

-- PRESENÇAS
INSERT INTO presenca (nr_alunos, data, monitoria_id)
VALUES (5, '2025-05-02', 1),
       (7, '2025-05-09', 1),
       (3, '2024-11-10', 2);

INSERT INTO disciplina (nome, codigo)
VALUES
    ('Banco de Dados', 'BD789'),
    ('Engenharia de Software', 'ES101'),
    ('Sistemas Operacionais', 'SO202'),
    ('Redes de Computadores', 'RC303'),
    ('Algoritmos e Lógica', 'AL404'),
    ('Programação para Aplicativos Móveis', 'PD233');

-- Assumindo que professor_id = 1 (Ana Professor), monitor_id = 2 (Carlos Monitor)
-- Usando também monitoria sem monitor atribuído
INSERT INTO monitoria (status, horario, ano, semestre, descricao, professor_id, monitor_id, disciplina_id)
VALUES
    ('ATIVA', 'Terças 13h', 2025, 1, 'Foco em comandos SQL', 1, 2, 3),
    ('ENCERRADA', 'Quintas 15h', 2024, 2, 'Encerrada após o semestre', 1, 2, 4),
    ('ABERTA', 'Sextas 9h', 2025, 1, null, 1, null, 5),
    ('ATIVA', 'Segundas 11h', 2025, 2, 'Atendimento contínuo', 1, 2, 2),
    ('ABERTA', 'Quartas 16h', 2024, 2, 'Disponível para inscrições', 1, null, 1),
    ('ABERTA', 'Quartas 16h', 2025, 2, 'Disponível para inscrições', 1, null, 1),
    ('ABERTA', 'Sextas 12h', 2025, 2, 'Disponível para inscrições', 1, null, 1),
    ('ATIVA', 'Terças 13h', 2025, 2, 'Em andamento', 6, 5, 5),
    ('ATIVA', 'Quintas 15h', 2024, 2, 'Foco em app móveis', 6, 5, 8);

-- Relacionadas às novas monitorias (IDs presumidos: 3 a 7)
INSERT INTO presenca (nr_alunos, data, monitoria_id)
VALUES
    (8, '2025-05-06', 3),
    (10, '2025-05-13', 3),
    (6, '2024-11-14', 4),
    (4, '2025-06-01', 5),
    (2, '2025-06-08', 5),
    (5, '2025-06-15', 6),
    (7, '2025-06-22', 6),
    (9, '2024-12-01', 7),
    (3, '2024-12-08', 7),
    (1, '2024-12-15', 7),
    (1, '2025-05-17', 5),
    (1, '2025-05-24', 10),
    (5, '2025-06-12', 10),
    (5, '2025-06-19', 10),
    (5, '2025-06-26', 10),
    (10, '2025-07-03', 11),
    (10, '2025-07-10', 11);
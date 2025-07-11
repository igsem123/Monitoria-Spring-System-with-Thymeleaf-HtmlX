package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Disciplina;
import br.com.iftm.monitoria.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository repository;

    public List<Disciplina> listarTodos() {
        return repository.findAll();
    }

    // Metodo para salvar disciplina
    public void salvar(Disciplina disciplina) {
        try {
            // Verifica se a disciplina é nula
            if (disciplina == null) {
                throw new IllegalArgumentException("Disciplina não pode ser nula.");
            }

            // Verifica se a disciplina já existe, se existir, informa o usuário com um throw
            if (disciplina.getId() != null) {
                Disciplina existente = buscarPorId(disciplina.getId());
                if (existente != null) {
                    // Se a disciplina já existe, informa o usuário
                    throw new IllegalArgumentException("Disciplina com ID " + disciplina.getId() + " já existe.");
                }
            }

            // Verifica se o nome da disciplina é válido
            if (disciplina.getNome() == null || disciplina.getNome().isEmpty()) {
                throw new IllegalArgumentException("O nome da disciplina não pode ser vazio.");
            }

            // Verifica se o código da disciplina é válido
            if (disciplina.getCodigo() == null || disciplina.getCodigo().isEmpty()) {
                throw new IllegalArgumentException("O código da disciplina não pode ser vazio.");
            }

            // Salva a disciplina
            repository.save(disciplina);

        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao salvar disciplina: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar disciplina: " + e.getMessage());
        }
    }

    public Disciplina buscarPorId(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("O ID da disciplina não pode ser nulo.");
            }

            return repository.findById(id).orElse(null);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao buscar disciplina: " + e.getMessage());
            return null;
        }
    }

    public void atualizar(Disciplina disciplina) {
        try {
            if (disciplina != null) {
                // Verifica se a disciplina já existe
                Disciplina existente = buscarPorId(disciplina.getId());

                // Se existir, atualiza os campos necessários
                if (existente != null) {
                    existente.setNome(disciplina.getNome());
                    existente.setCodigo(disciplina.getCodigo());

                    // Salva as alterações
                    repository.save(existente);
                }
            } else {
                throw new IllegalArgumentException("Disciplina inválida.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar disciplina: " + e.getMessage());
        }
    }

    public String gerarCodigoDisciplina(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da disciplina não pode ser vazio.");
        }

        String[] palavras = nome.trim().split("\\s+");
        StringBuilder iniciais = new StringBuilder();
        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                iniciais.append(Character.toUpperCase(palavra.charAt(0)));
            }
        }

        int numeros = (int) (Math.random() * 900) + 100;

        return "DISC-" + iniciais + numeros;
    }

    public void excluir(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("O ID da disciplina não pode ser nulo.");
            }

            Disciplina disciplina = buscarPorId(id);
            if (disciplina != null) {
                repository.delete(disciplina);
            } else {
                throw new IllegalArgumentException("Disciplina com ID " + id + " não encontrada.");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao excluir disciplina: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao excluir disciplina: " + e.getMessage());
        }
    }
}

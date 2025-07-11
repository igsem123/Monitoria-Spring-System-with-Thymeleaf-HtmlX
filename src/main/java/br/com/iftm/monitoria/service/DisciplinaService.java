package br.com.iftm.monitoria.service;

import br.com.iftm.monitoria.model.Disciplina;
import br.com.iftm.monitoria.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
        if (disciplina == null || disciplina.getId() == null) {
            throw new IllegalArgumentException("Disciplina inválida.");
        }

        Disciplina existente = buscarPorId(disciplina.getId());
        if (existente == null) {
            throw new RuntimeException("Disciplina não encontrada.");
        }

        existente.setNome(disciplina.getNome());
        existente.setCodigo(disciplina.getCodigo());

        repository.save(existente);
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

    public Page<Disciplina> listarTodosPaginado(PageRequest pageable) {
        // Obtém todas as disciplinas e ordena por ID
        List<Disciplina> disciplinas = repository.findAll()
            .stream()
            .sorted(Comparator.comparingLong(Disciplina::getId))
            .toList();

        // Verifica se a lista de disciplinas está vazia
        if (disciplinas.isEmpty()) {
            return Page.empty(pageable);
        }

        int total = disciplinas.size();
        int startItem = (int) pageable.getOffset();
        int end = Math.min((startItem + pageable.getPageSize()), total);

        List<Disciplina> paginaDisciplinas;
        if (startItem >= total) {
            return Page.empty(pageable);
        } else {
            paginaDisciplinas = disciplinas.subList(startItem, end);
        }

        return new PageImpl<>(paginaDisciplinas, pageable, total);
    }
}

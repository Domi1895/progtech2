package hu.nye.progtech.todolist.ToDoList.service;

import hu.nye.progtech.todolist.ToDoList.model.Todo;
import hu.nye.progtech.todolist.ToDoList.model.User;
import hu.nye.progtech.todolist.ToDoList.repository.TodoRepository;
import hu.nye.progtech.todolist.ToDoList.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Optional<Todo> findById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public List<Todo> findByUsername(String username) {
        return todoRepository.findByUserName(username);
    }

    public Todo addTodoToUser(String username, Todo todo) {
        final Optional<User> userOpt = userRepository.findByName(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Nincs ilyen felhasználó: " + username);
        }
        todo.setUser(userOpt.get());
        return todoRepository.save(todo);
    }

    public Optional<Todo> markAsCompleted(Long id) {
        final Optional<Todo> todoOpt = todoRepository.findById(id);
        todoOpt.ifPresent(todo -> {
            todo.setCompleted(true);
            todoRepository.save(todo);
        });
        return todoOpt;
    }

    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    public Optional<Todo> updateTodo(Long id, Todo updatedData) {
        return todoRepository.findById(id).map(existing -> {
            existing.setTitle(updatedData.getTitle());
            existing.setDescription(updatedData.getDescription());
            existing.setDueDate(updatedData.getDueDate());
            existing.setCompleted(updatedData.isCompleted());
            return todoRepository.save(existing);
        });
    }
}

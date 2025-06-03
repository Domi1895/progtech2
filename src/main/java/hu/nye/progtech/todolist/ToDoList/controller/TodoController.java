package hu.nye.progtech.todolist.ToDoList.controller;

import hu.nye.progtech.todolist.ToDoList.model.Todo;
import hu.nye.progtech.todolist.ToDoList.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    // Constructor injection
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.save(todo);
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.findAll();
    }

    @GetMapping("/user/{username}")
    public List<Todo> getTodosByUsername(@PathVariable String username) {
        return todoService.findByUsername(username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<Todo> createTodoForUser(@PathVariable String username, @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.addTodoToUser(username, todo));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Todo> markAsCompleted(@PathVariable Long id) {
        return todoService.markAsCompleted(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
        final Optional<Todo> todoOpt = todoService.updateTodo(id, updatedTodo);
        return todoOpt
                .map(todo -> ResponseEntity.ok(todo))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

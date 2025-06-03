package hu.nye.progtech.todolist.ToDoList.service;

import hu.nye.progtech.todolist.ToDoList.model.Todo;
import hu.nye.progtech.todolist.ToDoList.model.User;
import hu.nye.progtech.todolist.ToDoList.repository.TodoRepository;
import hu.nye.progtech.todolist.ToDoList.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    private Todo sampleTodo;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("testuser");

        sampleTodo = new Todo();
        sampleTodo.setId(1L);
        sampleTodo.setTitle("Test");
        sampleTodo.setDescription("Test Desc");
        sampleTodo.setDueDate(LocalDate.now());
        sampleTodo.setCompleted(false);
        sampleTodo.setUser(sampleUser);
    }

    @Test
    void testFindAll() {
        when(todoRepository.findAll()).thenReturn(List.of(sampleTodo));
        List<Todo> result = todoService.findAll();
        assertEquals(1, result.size());
        verify(todoRepository).findAll();
    }

    @Test
    void testFindById() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        Optional<Todo> result = todoService.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTitle());
    }

    @Test
    void testSave() {
        when(todoRepository.save(sampleTodo)).thenReturn(sampleTodo);
        Todo result = todoService.save(sampleTodo);
        assertNotNull(result);
        verify(todoRepository).save(sampleTodo);
    }

    @Test
    void testFindByUsername() {
        when(todoRepository.findByUserName("testuser")).thenReturn(List.of(sampleTodo));
        List<Todo> result = todoService.findByUsername("testuser");
        assertEquals(1, result.size());
    }

    @Test
    void testAddTodoToUser_userExists() {
        when(userRepository.findByName("testuser")).thenReturn(Optional.of(sampleUser));
        when(todoRepository.save(any(Todo.class))).thenReturn(sampleTodo);

        Todo result = todoService.addTodoToUser("testuser", sampleTodo);

        assertEquals(sampleUser, result.getUser());
        verify(todoRepository).save(sampleTodo);
    }

    @Test
    void testAddTodoToUser_userNotFound() {
        when(userRepository.findByName("unknown")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            todoService.addTodoToUser("unknown", sampleTodo);
        });

        assertTrue(exception.getMessage().contains("Nincs ilyen felhasználó"));
    }

    @Test
    void testMarkAsCompleted_found() {
        sampleTodo.setCompleted(false);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        when(todoRepository.save(sampleTodo)).thenReturn(sampleTodo);

        Optional<Todo> result = todoService.markAsCompleted(1L);

        assertTrue(result.isPresent());
        assertTrue(result.get().isCompleted());
        verify(todoRepository).save(sampleTodo);
    }

    @Test
    void testDeleteById() {
        todoService.deleteById(1L);
        verify(todoRepository).deleteById(1L);
    }

    @Test
    void testUpdateTodo_found() {
        Todo updated = new Todo();
        updated.setTitle("Updated");
        updated.setDescription("New Desc");
        updated.setDueDate(LocalDate.now().plusDays(1));
        updated.setCompleted(true);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(sampleTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(sampleTodo);

        Optional<Todo> result = todoService.updateTodo(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getTitle());
        verify(todoRepository).save(sampleTodo);
    }
}

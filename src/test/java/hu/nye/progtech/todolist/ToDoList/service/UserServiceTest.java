package hu.nye.progtech.todolist.ToDoList.service;

import hu.nye.progtech.todolist.ToDoList.model.User;
import hu.nye.progtech.todolist.ToDoList.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);
        user1.setName("TestUser");
    }

    @Test
    void findAllShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findByIdShouldReturnUserWhenFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdShouldReturnEmptyWhenNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(2L);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void saveShouldReturnSavedUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User savedUser = userService.save(user1);

        assertNotNull(savedUser);
        assertEquals(user1, savedUser);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void deleteByIdShouldCallRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}

package hu.nye.progtech.todolist.ToDoList.repository;

import hu.nye.progtech.todolist.ToDoList.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserName(String username);
}

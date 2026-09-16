package com.example.taskAPI;

import com.example.taskAPI.model.Task;
import com.example.taskAPI.repository.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Enhetstest för TaskRepository som verifierar att datalagringslogiken fungerar.
public class TaskRepositoryTest {

    private final TaskRepository repository = new TaskRepository();

    @Test
    // Arrange: repositoryn innehåller redan seedade tasks.
    // Act: anropa findAll().
    // Assert: listan innehåller 5 tasks och har rätt namn.
    void shouldReturnSeededTasks() {
        List<Task> tasks = repository.findAll();

        assertEquals(5, tasks.size());
        assertEquals("äpple", tasks.get(0).getName());
        assertEquals("citron", tasks.get(2).getName());
    }

    @Test
    // Arrange: skapa en ny task som ska sparas.
    // Act: anropa save(task).
    // Assert: listan växer och tasken kan hittas med id.
    void shouldSaveTask() {
        Task task = new Task(99, "jordgubbar", false);

        repository.save(task);

        assertEquals(6, repository.findAll().size());
        assertNotNull(repository.findById(99));
        assertEquals("jordgubbar", repository.findById(99).getName());
    }

    @Test
    // Arrange: använd ett känt id från repositoryn.
    // Act: anropa findById(2).
    // Assert: rätt task returneras.
    void shouldFindTaskById() {
        Task task = repository.findById(2);

        assertNotNull(task);
        assertEquals(2, task.getId());
        assertEquals("mjölk", task.getName());
    }

    @Test
    // Arrange: skapa en task i repositoryn och radera den sedan.
    // Act: anropa delete(2).
    // Assert: tasken finns inte längre i listan.
    void shouldDeleteTaskById() {
        repository.delete(2);

        assertNull(repository.findById(2));
        assertEquals(4, repository.findAll().size());
    }
}

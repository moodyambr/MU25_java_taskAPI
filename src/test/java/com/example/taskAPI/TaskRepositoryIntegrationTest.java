package com.example.taskAPI;

import com.example.taskAPI.model.Task;
import com.example.taskAPI.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

// Testklass som verifierar repository-lagrets grundläggande funktionalitet.
class TaskRepositoryIntegrationTest {

    private final TaskRepository repository = new TaskRepository();

    @Test
    // Arrange: repositoryn har redan fördefinierade tasks.
    // Act: anropa findAll().
    // Assert: lista innehåller 5 tasks och rätt namn på positionerna.
    void shouldReturnSeededTasks() {
        List<Task> tasks = repository.findAll();

        assertEquals(5, tasks.size());
        assertEquals("äpple", tasks.get(0).getName());
        assertEquals("citron", tasks.get(2).getName());
    }

    @Test
    // Arrange: skapa en ny task som ska sparas.
    // Act: spara tasken i repositoryn.
    // Assert: antal tasks ökar och den går att hämta med id.
    void shouldSaveNewTask() {
        Task task = new Task(99, "jordgubbar", false);

        repository.save(task);

        assertEquals(6, repository.findAll().size());
        assertEquals("jordgubbar", repository.findById(99).getName());
    }

    @Test
    // Arrange: använd ett känt id från startdata.
    // Act: hitta task med id 3.
    // Assert: rätt task returneras.
    void shouldFindTaskById() {
        Task task = repository.findById(3);

        assertNotNull(task);
        assertEquals(3, task.getId());
        assertEquals("citron", task.getName());
    }

    @Test
    // Arrange: skapa en task som finns i listan, sedan radera den.
    // Act: anropa delete(2).
    // Assert: tasken saknas och listan blir kortare.
    void shouldDeleteTaskById() {
        repository.delete(2);

        assertNull(repository.findById(2));
        assertEquals(4, repository.findAll().size());
    }
}

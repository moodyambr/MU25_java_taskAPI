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
    // Kontrollera att startdata finns i repositoryn.
    void shouldReturnSeededTasks() {
        List<Task> tasks = repository.findAll();

        assertEquals(5, tasks.size());
        assertEquals("äpple", tasks.get(0).getName());
        assertEquals("citron", tasks.get(2).getName());
    }

    @Test
    // Lägg till en ny task och kontrollera att den sparas.
    void shouldSaveNewTask() {
        Task task = new Task(99, "jordgubbar", false);

        repository.save(task);

        assertEquals(6, repository.findAll().size());
        assertEquals("jordgubbar", repository.findById(99).getName());
    }

    @Test
    // Hämta en task via id och verifiera att rätt objekt returneras.
    void shouldFindTaskById() {
        Task task = repository.findById(3);

        assertNotNull(task);
        assertEquals(3, task.getId());
        assertEquals("citron", task.getName());
    }

    @Test
    // Ta bort en task och kontrollera att den inte längre finns.
    void shouldDeleteTaskById() {
        repository.delete(2);

        assertNull(repository.findById(2));
        assertEquals(4, repository.findAll().size());
    }
}

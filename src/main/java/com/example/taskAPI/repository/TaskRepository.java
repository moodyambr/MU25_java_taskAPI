package com.example.taskAPI.repository;

import com.example.taskAPI.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
// Simulerar ett datalager för tasks i minnet.
public class TaskRepository {
    // Lista som lagrar alla uppgifter under körningen.
    private List<Task> tasks = new ArrayList<>();

    public TaskRepository() {
        // Lägger in några standarduppgifter så att appen har testdata från början.
        tasks.add(new Task(1, "äpple", false));
        tasks.add(new Task(2, "mjölk", true));
        tasks.add(new Task(3, "citron", false));
        tasks.add(new Task(4, "kalan", false));
        tasks.add(new Task(5, "banan", true));
    }

    public List<Task> findAll() {
        // Returnerar hela listan med uppgifter.
        return tasks;
    }

    public void save(Task task) {
        // Sparar en ny task i listan.
        tasks.add(task);
    }

    public void delete(int id) {
        // Tar bort alla tasks som har det angivna id:t.
        tasks.removeIf(task -> task.getId() == id);
    }

    public Task findById(int id) {
        // Hittar den första tasken med matchande id, annars null.
        return tasks.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }
}

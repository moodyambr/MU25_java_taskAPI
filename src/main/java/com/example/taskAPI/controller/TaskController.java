package com.example.taskAPI.controller;

import com.example.taskAPI.model.Task;
import com.example.taskAPI.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// Exponerar REST-endpoints för att läsa, lägga till, uppdatera och ta bort tasks.
public class TaskController {

    private TaskRepository repository;

    public TaskController(TaskRepository repository) {
        // Sparar repository för att använda datalager i varje endpoint.
        this.repository = repository;
    }

    @GetMapping("/tasks")
    // Hämtar hela listan med tasks och returnerar den som JSON.
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/tasks")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    // Skapar en ny task om användaren är inloggad som USER eller ADMIN.
    public ResponseEntity<Task> addTask(@Valid @RequestBody Task task) {
        repository.save(task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // Tar bort en task om den finns och kräver ADMIN-behörighet.
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        Task task = repository.findById(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        repository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tasks/{id}")
    // Uppdaterar bara de fält som skickas in i request-body.
    public ResponseEntity<Task> patchTask(@PathVariable int id, @RequestBody Task updates) {
        Task task = repository.findById(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        if (updates.getName() != null) {
            task.setName(updates.getName());
        }

        if (updates.isDone() != null) {
            task.setDone(updates.isDone());
        }

        return ResponseEntity.ok(task);
    }

}

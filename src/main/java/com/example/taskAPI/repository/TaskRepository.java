package com.example.taskAPI.repository;

import com.example.taskAPI.model.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {
    private List<Task> tasks = new ArrayList<>();

    public TaskRepository() {
        tasks.add(new Task(1, "äpple", false));
        tasks.add(new Task(2, "mjölk", true));
        tasks.add(new Task(3, "citron", false));
    }

    public List<Task> findAll() {
        return tasks;
    }

    public void save(Task task) {
        tasks.add(task);
    }

    public void delete(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    public Task findById(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }





}

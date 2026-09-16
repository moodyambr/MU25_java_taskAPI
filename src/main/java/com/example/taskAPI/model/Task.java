package com.example.taskAPI.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// En task representerar ett enkelt todo-objekt med id, namn och status.
public class Task {
    // Unikt id för varje task.
    private Integer id;

    @NotBlank(message = "Name have to have a value")
    @Size(min = 2, message = "Name must be at least 2 characters")
    // Namnet som visas för användaren.
    private String name;

    @NotNull(message = "done need to be true or false")
    // true = klar, false = inte klar.
    private Boolean done;

    //    @Email(message = "email invalid")
    //    private String email;

    public Task() {
    }

    public Task(Integer id, String name, Boolean done) {
        // Konstruktor för att skapa en färdig task direkt.
        this.id = id;
        this.name = name;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}

package com.example.taskAPI;

import com.example.taskAPI.security.JwtService;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import com.example.taskAPI.controller.TaskController;
import com.example.taskAPI.model.Task;
import com.example.taskAPI.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
// Web-layer-tester för TaskController utan att använda riktig databas.
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskRepository repository;

    @MockitoBean
    private JwtService jwtService;

    @Test
    // Arrange: skapa en task och mocka repository.findAll() att returnera den.
    // Act: gör GET /tasks.
    // Assert: status 200 och att det första namnet är "Köp mjölk".
    void shouldReturnAllTasks() throws Exception {
        Task task = new Task(1, "Köp mjölk", false);

        when(repository.findAll()).thenReturn(List.of(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Köp mjölk"));
    }

    @Test
    // Arrange: skapa JSON för en ny task.
    // Act: gör POST /tasks.
    // Assert: status 201 och att repository.save() anropas en gång.
    void shouldAddNewTaskAndReturnCreated() throws Exception {
        String jsonRequest = "{ \"id\" : 1, \"name\" : \"Städa\", \"done\": false  }";

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Städa"));

        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    // Arrange: mocka repository.findById(1) så att tasken finns.
    // Act: gör DELETE /tasks/1.
    // Assert: status 204 och repository.delete(1) anropas.
    void shouldDeleteTaskWhenFound() throws Exception {
        Task task = new Task();
        when(repository.findById(1)).thenReturn(task);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(repository).delete(1);
    }

    @Test
    // Arrange: mocka repository.findById(99) till null.
    // Act: gör DELETE /tasks/99.
    // Assert: status 404 och repository.delete() ska aldrig anropas.
    void shouldReturnNotFoundWhenDeletingNonExistingTask() throws Exception {
        when(repository.findById(99)).thenReturn(null);

        mockMvc.perform(delete("/tasks/99"))
                .andExpect(status().isNotFound());

        verify(repository, never()).delete(anyInt());
    }

    @Test
    // Arrange: skapa en befintlig task med gammalt namn och mocka repository.findById(1).
    // Act: gör PATCH /tasks/1 med nytt namn.
    // Assert: status 200 och att namnet har uppdaterats.
    void shouldPatchTaskNameSuccessfully() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setName("Gammalt namn");
        existingTask.setDone(false);
        when(repository.findById(1)).thenReturn(existingTask);

        String patchJson = "{\"name\": \"Nytt namn\"}";

        mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nytt namn"));
    }
}

package com.example.taskAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
// Integrationstester för TaskController med riktig Spring-context och HTTP-anrop.
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    //@BeforeEach
    //void setup() {
    //    // Setup code if needed

    @Test
    // GET /tasks ska returnera status 200 och alla fördefinierade tasks.
    void getAllTasks_ShouldReturn200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name", is("äpple")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    // ADMIN får ta bort en task och får 204 No Content.
    void deleteTaskById_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    // USER får skapa en ny task och får 201 Created med rätt data tillbaka.
    void addTask_ShouldReturn201() throws Exception {
        String newTaskJson = "{ \"id\": 99, \"name\": \"New Task\", \"done\": false }";

        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(newTaskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(99)))
                .andExpect(jsonPath("$.name", is("New Task")))
                .andExpect(jsonPath("$.done", is(false)));
    }
}

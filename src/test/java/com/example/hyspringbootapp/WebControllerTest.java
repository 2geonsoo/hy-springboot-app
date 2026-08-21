package com.example.hyspringbootapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = WebController.class, properties = {
        "app.version=1.2.3",
        "app.private-address=10.0.1.23"
})
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homeReturnsMessage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Spring Boot!"))
                .andExpect(jsonPath("$.version").value("1.2.3"))
                .andExpect(jsonPath("$.privateAddress").value("10.0.1.23"));
    }

    @Test
    void healthReturnsUp() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void timecheckReturnsCurrentTime() throws Exception {
        mockMvc.perform(get("/timecheck"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.time").isString());
    }
}

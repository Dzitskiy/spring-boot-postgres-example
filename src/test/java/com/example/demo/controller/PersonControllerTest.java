package com.example.demo.controller;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPersons_ShouldReturnList() throws Exception {
        Person p1 = new Person(1L, "John", "john@example.com");
        Person p2 = new Person(2L, "Jane", "jane@example.com");
        when(personService.findAll()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void getPersonById_WhenExists_ShouldReturnPerson() throws Exception {
        Person p = new Person(1L, "John", "john@example.com");
        when(personService.findById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void getPersonById_WhenNotExists_ShouldReturn404() throws Exception {
        when(personService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/persons/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPerson_ShouldReturnCreated() throws Exception {
        Person input = new Person(null, "New", "new@example.com");
        Person saved = new Person(3L, "New", "new@example.com");
        when(personService.save(any(Person.class))).thenReturn(saved);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    void updatePerson_WhenExists_ShouldReturnUpdated() throws Exception {
        Person input = new Person(null, "Updated", "updated@example.com");
        Person updated = new Person(1L, "Updated", "updated@example.com");
        when(personService.update(1L, input)).thenReturn(updated);

        mockMvc.perform(put("/api/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void updatePerson_WhenNotExists_ShouldReturn404() throws Exception {
        Person input = new Person(null, "Updated", "updated@example.com");
        when(personService.update(99L, input)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/api/persons/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePerson_WhenExists_ShouldReturn204() throws Exception {
        when(personService.findById(1L)).thenReturn(Optional.of(new Person(1L, "John", "john@example.com")));

        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePerson_WhenNotExists_ShouldReturn404() throws Exception {
        when(personService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/persons/99"))
                .andExpect(status().isNotFound());
    }
}
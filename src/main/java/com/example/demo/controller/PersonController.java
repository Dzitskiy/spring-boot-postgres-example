package com.example.demo.controller;

import com.example.demo.entity.Person;
import com.example.demo.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@Tag(name = "Person Controller", description = "Endpoints for managing persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    @Operation(summary = "Get all persons", description = "Returns a list of all persons")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public List<Person> getAllPersons() {
        return personService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get person by ID", description = "Returns a single person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "404", description = "Person not found", content = @Content)
    })
    public ResponseEntity<Person> getPersonById(
            @Parameter(description = "ID of the person to retrieve") @PathVariable Long id) {
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person) {
        return personService.save(person);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        try {
            Person updated = personService.update(id, person);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (personService.findById(id).isPresent()) {
            personService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
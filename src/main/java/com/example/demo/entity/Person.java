package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Person entity")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Unique identifier", example = "1")
    private Long id;
    
    @Column(nullable = false)
    @Schema(description = "Person's full name", example = "John Doe", required = true)
    private String name;
    
    @Column(nullable = false, unique = true)
    @Schema(description = "Email address", example = "john@example.com", required = true)
    private String email;
}
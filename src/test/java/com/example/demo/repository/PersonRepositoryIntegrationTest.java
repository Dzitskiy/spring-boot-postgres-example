package com.example.demo.repository;

import com.example.demo.entity.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private PersonRepository personRepository;

    @Test
    void saveAndFindById_ShouldWork() {
        Person person = new Person(null, "Test User", "test@example.com");
        Person saved = personRepository.save(person);

        Optional<Person> found = personRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test User");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void delete_ShouldRemovePerson() {
        Person person = personRepository.save(new Person(null, "To Delete", "delete@example.com"));
        assertThat(personRepository.findById(person.getId())).isPresent();

        personRepository.deleteById(person.getId());

        assertThat(personRepository.findById(person.getId())).isEmpty();
    }
}
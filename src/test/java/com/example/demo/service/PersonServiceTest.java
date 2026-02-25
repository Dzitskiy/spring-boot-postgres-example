package com.example.demo.service;

import com.example.demo.entity.Person;
import com.example.demo.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person(1L, "John Doe", "john@example.com");
    }

    @Test
    void save_ShouldReturnSavedPerson() {
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person saved = personService.save(person);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void findById_WhenExists_ShouldReturnPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Optional<Person> found = personService.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Person> found = personService.findById(99L);

        assertThat(found).isEmpty();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(personRepository).deleteById(1L);

        personService.deleteById(1L);

        verify(personRepository, times(1)).deleteById(1L);
    }

    @Test
    void update_WhenExists_ShouldUpdateAndReturn() {
        Person updatedDetails = new Person(null, "Jane Doe", "jane@example.com");
        Person existing = new Person(1L, "John Doe", "john@example.com");
        when(personRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Person updated = personService.update(1L, updatedDetails);

        assertThat(updated.getName()).isEqualTo("Jane Doe");
        assertThat(updated.getEmail()).isEqualTo("jane@example.com");
        verify(personRepository).save(existing);
    }

    @Test
    void update_WhenNotExists_ShouldThrowException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> personService.update(99L, new Person()));
    }
}
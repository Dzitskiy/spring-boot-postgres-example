package com.example.demo.service;

import com.example.demo.entity.Person;
import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> findAll();
    Optional<Person> findById(Long id);
    Person save(Person person);
    void deleteById(Long id);
    Person update(Long id, Person person);
}
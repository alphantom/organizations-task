package com.albina.springproject.repositories;

import com.albina.springproject.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    @Query("SELECT p FROM Person p " +
            "LEFT JOIN FETCH p.country " +
            "LEFT JOIN FETCH p.document WHERE p.id = ?1")
    public Person findById(long id);
}

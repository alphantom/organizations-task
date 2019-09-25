package com.albina.springproject.repositories;

import com.albina.springproject.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByNumber(String name);
}

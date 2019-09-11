package com.albina.springproject.repositories.catalog;

import com.albina.springproject.models.catalog.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}

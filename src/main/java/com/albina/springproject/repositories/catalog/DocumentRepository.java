package com.albina.springproject.repositories.catalog;

import com.albina.springproject.models.catalog.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DocumentRepository extends JpaRepository<Document, Byte> {
}

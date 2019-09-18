package com.albina.springproject.repositories.catalog;

import com.albina.springproject.models.catalog.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentType, Byte> {
}

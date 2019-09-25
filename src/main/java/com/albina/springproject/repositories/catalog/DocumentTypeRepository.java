package com.albina.springproject.repositories.catalog;

import com.albina.springproject.models.catalog.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Byte> {

    Optional<DocumentType> findByName(String name);
    Optional<DocumentType> findByCode(byte code);
}

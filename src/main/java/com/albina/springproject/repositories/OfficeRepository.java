package com.albina.springproject.repositories;

import com.albina.springproject.models.Office;
import com.albina.springproject.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface OfficeRepository extends JpaRepository<Office, Long>, JpaSpecificationExecutor<Office> {
    @Query("SELECT o FROM Office o JOIN FETCH o.organizations WHERE o.id = ?1")
    public Office findById(long id);
}

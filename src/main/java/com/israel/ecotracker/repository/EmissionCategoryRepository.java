package com.israel.ecotracker.repository;

import com.israel.ecotracker.domain.EmissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmissionCategoryRepository extends JpaRepository<EmissionCategory, Long> {

    // Find a category by its name (e.g., "GAS_CAR", "ELECTRICITY")
    Optional<EmissionCategory> findByName(String name);
}
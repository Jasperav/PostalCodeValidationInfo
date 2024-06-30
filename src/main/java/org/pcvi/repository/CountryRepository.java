package org.pcvi.repository;

import org.pcvi.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Simple repository for the Country model to work with JPA.
 */
public interface CountryRepository extends JpaRepository<Country, String> { }
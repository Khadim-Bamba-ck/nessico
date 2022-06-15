package com.smm.smo.repository;

import com.smm.smo.domain.Nessico;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Nessico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NessicoRepository extends JpaRepository<Nessico, Long> {}

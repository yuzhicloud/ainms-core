package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.NCEAPStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NCEAPStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NCEAPStatusRepository
  extends JpaRepository<NCEAPStatus, Integer> {}

package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessController;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccessController entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessControllerRepository
  extends JpaRepository<AccessController, Long> {}

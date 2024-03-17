package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccessPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessPointRepository extends JpaRepository<AccessPoint, Long> {}

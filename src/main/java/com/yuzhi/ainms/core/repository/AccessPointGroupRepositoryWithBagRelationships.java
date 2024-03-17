package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPointGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AccessPointGroupRepositoryWithBagRelationships {
    Optional<AccessPointGroup> fetchBagRelationships(Optional<AccessPointGroup> accessPointGroup);

    List<AccessPointGroup> fetchBagRelationships(List<AccessPointGroup> accessPointGroups);

    Page<AccessPointGroup> fetchBagRelationships(Page<AccessPointGroup> accessPointGroups);
}

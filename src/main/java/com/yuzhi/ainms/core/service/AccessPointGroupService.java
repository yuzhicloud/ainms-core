package com.yuzhi.ainms.core.service;

import com.yuzhi.ainms.core.domain.AccessPointGroup;
import com.yuzhi.ainms.core.repository.AccessPointGroupRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yuzhi.ainms.core.domain.AccessPointGroup}.
 */
@Service
@Transactional
public class AccessPointGroupService {

  private final Logger log = LoggerFactory.getLogger(
    AccessPointGroupService.class
  );

  private final AccessPointGroupRepository accessPointGroupRepository;

  public AccessPointGroupService(
    AccessPointGroupRepository accessPointGroupRepository
  ) {
    this.accessPointGroupRepository = accessPointGroupRepository;
  }

  /**
   * Save a accessPointGroup.
   *
   * @param accessPointGroup the entity to save.
   * @return the persisted entity.
   */
  public AccessPointGroup save(AccessPointGroup accessPointGroup) {
    log.debug("Request to save AccessPointGroup : {}", accessPointGroup);
    return accessPointGroupRepository.save(accessPointGroup);
  }

  /**
   * Update a accessPointGroup.
   *
   * @param accessPointGroup the entity to save.
   * @return the persisted entity.
   */
  public AccessPointGroup update(AccessPointGroup accessPointGroup) {
    log.debug("Request to update AccessPointGroup : {}", accessPointGroup);
    return accessPointGroupRepository.save(accessPointGroup);
  }

  /**
   * Partially update a accessPointGroup.
   *
   * @param accessPointGroup the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<AccessPointGroup> partialUpdate(
    AccessPointGroup accessPointGroup
  ) {
    log.debug(
      "Request to partially update AccessPointGroup : {}",
      accessPointGroup
    );

    return accessPointGroupRepository
      .findById(accessPointGroup.getId())
      .map(existingAccessPointGroup -> {
        if (accessPointGroup.getName() != null) {
          existingAccessPointGroup.setName(accessPointGroup.getName());
        }

        return existingAccessPointGroup;
      })
      .map(accessPointGroupRepository::save);
  }

  /**
   * Get all the accessPointGroups.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<AccessPointGroup> findAll(Pageable pageable) {
    log.debug("Request to get all AccessPointGroups");
    return accessPointGroupRepository.findAll(pageable);
  }

  /**
   * Get one accessPointGroup by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AccessPointGroup> findOne(Long id) {
    log.debug("Request to get AccessPointGroup : {}", id);
    return accessPointGroupRepository.findById(id);
  }

  /**
   * Delete the accessPointGroup by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete AccessPointGroup : {}", id);
    accessPointGroupRepository.deleteById(id);
  }
}

package com.yuzhi.ainms.core.service;

import com.yuzhi.ainms.core.domain.AccessController;
import com.yuzhi.ainms.core.repository.AccessControllerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yuzhi.ainms.core.domain.AccessController}.
 */
@Service
@Transactional
public class AccessControllerService {

  private final Logger log = LoggerFactory.getLogger(
    AccessControllerService.class
  );

  private final AccessControllerRepository accessControllerRepository;

  public AccessControllerService(
    AccessControllerRepository accessControllerRepository
  ) {
    this.accessControllerRepository = accessControllerRepository;
  }

  /**
   * Save a accessController.
   *
   * @param accessController the entity to save.
   * @return the persisted entity.
   */
  public AccessController save(AccessController accessController) {
    log.debug("Request to save AccessController : {}", accessController);
    return accessControllerRepository.save(accessController);
  }

  /**
   * Update a accessController.
   *
   * @param accessController the entity to save.
   * @return the persisted entity.
   */
  public AccessController update(AccessController accessController) {
    log.debug("Request to update AccessController : {}", accessController);
    return accessControllerRepository.save(accessController);
  }

  /**
   * Partially update a accessController.
   *
   * @param accessController the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<AccessController> partialUpdate(
    AccessController accessController
  ) {
    log.debug(
      "Request to partially update AccessController : {}",
      accessController
    );

    return accessControllerRepository
      .findById(accessController.getId())
      .map(existingAccessController -> {
        if (accessController.getNedn() != null) {
          existingAccessController.setNedn(accessController.getNedn());
        }
        if (accessController.getNeid() != null) {
          existingAccessController.setNeid(accessController.getNeid());
        }
        if (accessController.getAliasname() != null) {
          existingAccessController.setAliasname(
            accessController.getAliasname()
          );
        }
        if (accessController.getNename() != null) {
          existingAccessController.setNename(accessController.getNename());
        }
        if (accessController.getNecategory() != null) {
          existingAccessController.setNecategory(
            accessController.getNecategory()
          );
        }
        if (accessController.getNetype() != null) {
          existingAccessController.setNetype(accessController.getNetype());
        }
        if (accessController.getNevendorname() != null) {
          existingAccessController.setNevendorname(
            accessController.getNevendorname()
          );
        }
        if (accessController.getNeesn() != null) {
          existingAccessController.setNeesn(accessController.getNeesn());
        }
        if (accessController.getNeip() != null) {
          existingAccessController.setNeip(accessController.getNeip());
        }
        if (accessController.getNemac() != null) {
          existingAccessController.setNemac(accessController.getNemac());
        }
        if (accessController.getVersion() != null) {
          existingAccessController.setVersion(accessController.getVersion());
        }
        if (accessController.getNestate() != null) {
          existingAccessController.setNestate(accessController.getNestate());
        }
        if (accessController.getCreatetime() != null) {
          existingAccessController.setCreatetime(
            accessController.getCreatetime()
          );
        }
        if (accessController.getNeiptype() != null) {
          existingAccessController.setNeiptype(accessController.getNeiptype());
        }
        if (accessController.getSubnet() != null) {
          existingAccessController.setSubnet(accessController.getSubnet());
        }
        if (accessController.getNeosversion() != null) {
          existingAccessController.setNeosversion(
            accessController.getNeosversion()
          );
        }

        return existingAccessController;
      })
      .map(accessControllerRepository::save);
  }

  /**
   * Get all the accessControllers.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public Page<AccessController> findAll(Pageable pageable) {
    log.debug("Request to get all AccessControllers");
    return accessControllerRepository.findAll(pageable);
  }

  /**
   * Get one accessController by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AccessController> findOne(Long id) {
    log.debug("Request to get AccessController : {}", id);
    return accessControllerRepository.findById(id);
  }

  /**
   * Delete the accessController by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete AccessController : {}", id);
    accessControllerRepository.deleteById(id);
  }
}

package com.yuzhi.ainms.core.service;

import com.yuzhi.ainms.core.domain.PowerPlant;
import com.yuzhi.ainms.core.repository.PowerPlantRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yuzhi.ainms.core.domain.PowerPlant}.
 */
@Service
@Transactional
public class PowerPlantService {

  private final Logger log = LoggerFactory.getLogger(PowerPlantService.class);

  private final PowerPlantRepository powerPlantRepository;

  public PowerPlantService(PowerPlantRepository powerPlantRepository) {
    this.powerPlantRepository = powerPlantRepository;
  }

  /**
   * Save a powerPlant.
   *
   * @param powerPlant the entity to save.
   * @return the persisted entity.
   */
  public PowerPlant save(PowerPlant powerPlant) {
    log.debug("Request to save PowerPlant : {}", powerPlant);
    return powerPlantRepository.save(powerPlant);
  }

  /**
   * Update a powerPlant.
   *
   * @param powerPlant the entity to save.
   * @return the persisted entity.
   */
  public PowerPlant update(PowerPlant powerPlant) {
    log.debug("Request to update PowerPlant : {}", powerPlant);
    return powerPlantRepository.save(powerPlant);
  }

  /**
   * Partially update a powerPlant.
   *
   * @param powerPlant the entity to update partially.
   * @return the persisted entity.
   */
  public Optional<PowerPlant> partialUpdate(PowerPlant powerPlant) {
    log.debug("Request to partially update PowerPlant : {}", powerPlant);

    return powerPlantRepository
      .findById(powerPlant.getId())
      .map(existingPowerPlant -> {
        if (powerPlant.getPowerPlantName() != null) {
          existingPowerPlant.setPowerPlantName(powerPlant.getPowerPlantName());
        }

        return existingPowerPlant;
      })
      .map(powerPlantRepository::save);
  }

  /**
   * Get all the powerPlants.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<PowerPlant> findAll() {
    log.debug("Request to get all PowerPlants");
    return powerPlantRepository.findAll();
  }

  /**
   * Get one powerPlant by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<PowerPlant> findOne(Long id) {
    log.debug("Request to get PowerPlant : {}", id);
    return powerPlantRepository.findById(id);
  }

  /**
   * Delete the powerPlant by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete PowerPlant : {}", id);
    powerPlantRepository.deleteById(id);
  }
}

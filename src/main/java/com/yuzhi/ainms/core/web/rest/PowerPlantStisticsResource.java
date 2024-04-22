package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import com.yuzhi.ainms.core.domain.ProvinceStistics;
import com.yuzhi.ainms.core.repository.PowerPlantStisticsRepository;
import com.yuzhi.ainms.core.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yuzhi.ainms.core.domain.PowerPlantStistics}.
 */
@RestController
@RequestMapping("/api/power-plant-stistics")
@Transactional
public class PowerPlantStisticsResource {

  private final Logger log = LoggerFactory.getLogger(
    PowerPlantStisticsResource.class
  );

  private static final String ENTITY_NAME = "powerPlantStistics";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PowerPlantStisticsRepository powerPlantStisticsRepository;

  public PowerPlantStisticsResource(
    PowerPlantStisticsRepository powerPlantStisticsRepository
  ) {
    this.powerPlantStisticsRepository = powerPlantStisticsRepository;
  }

  /**
   * {@code POST  /power-plant-stistics} : Create a new powerPlantStistics.
   *
   * @param powerPlantStistics the powerPlantStistics to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new powerPlantStistics, or with status {@code 400 (Bad Request)} if the powerPlantStistics has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  public ResponseEntity<PowerPlantStistics> createPowerPlantStistics(
    @Valid @RequestBody PowerPlantStistics powerPlantStistics
  ) throws URISyntaxException {
    log.debug(
      "REST request to save PowerPlantStistics : {}",
      powerPlantStistics
    );
    if (powerPlantStistics.getId() != null) {
      throw new BadRequestAlertException(
        "A new powerPlantStistics cannot already have an ID",
        ENTITY_NAME,
        "idexists"
      );
    }
    powerPlantStistics = powerPlantStisticsRepository.save(powerPlantStistics);
    return ResponseEntity.created(
      new URI("/api/power-plant-stistics/" + powerPlantStistics.getId())
    )
      .headers(
        HeaderUtil.createEntityCreationAlert(
          applicationName,
          true,
          ENTITY_NAME,
          powerPlantStistics.getId().toString()
        )
      )
      .body(powerPlantStistics);
  }

  /**
   * {@code PUT  /power-plant-stistics/:id} : Updates an existing powerPlantStistics.
   *
   * @param id the id of the powerPlantStistics to save.
   * @param powerPlantStistics the powerPlantStistics to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerPlantStistics,
   * or with status {@code 400 (Bad Request)} if the powerPlantStistics is not valid,
   * or with status {@code 500 (Internal Server Error)} if the powerPlantStistics couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  public ResponseEntity<PowerPlantStistics> updatePowerPlantStistics(
    @PathVariable(value = "id", required = false) final Long id,
    @Valid @RequestBody PowerPlantStistics powerPlantStistics
  ) throws URISyntaxException {
    log.debug(
      "REST request to update PowerPlantStistics : {}, {}",
      id,
      powerPlantStistics
    );
    if (powerPlantStistics.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, powerPlantStistics.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!powerPlantStisticsRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    powerPlantStistics = powerPlantStisticsRepository.save(powerPlantStistics);
    return ResponseEntity.ok()
      .headers(
        HeaderUtil.createEntityUpdateAlert(
          applicationName,
          true,
          ENTITY_NAME,
          powerPlantStistics.getId().toString()
        )
      )
      .body(powerPlantStistics);
  }

  /**
   * {@code PATCH  /power-plant-stistics/:id} : Partial updates given fields of an existing powerPlantStistics, field will ignore if it is null
   *
   * @param id the id of the powerPlantStistics to save.
   * @param powerPlantStistics the powerPlantStistics to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated powerPlantStistics,
   * or with status {@code 400 (Bad Request)} if the powerPlantStistics is not valid,
   * or with status {@code 404 (Not Found)} if the powerPlantStistics is not found,
   * or with status {@code 500 (Internal Server Error)} if the powerPlantStistics couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(
    value = "/{id}",
    consumes = { "application/json", "application/merge-patch+json" }
  )
  public ResponseEntity<PowerPlantStistics> partialUpdatePowerPlantStistics(
    @PathVariable(value = "id", required = false) final Long id,
    @NotNull @RequestBody PowerPlantStistics powerPlantStistics
  ) throws URISyntaxException {
    log.debug(
      "REST request to partial update PowerPlantStistics partially : {}, {}",
      id,
      powerPlantStistics
    );
    if (powerPlantStistics.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, powerPlantStistics.getId())) {
      throw new BadRequestAlertException(
        "Invalid ID",
        ENTITY_NAME,
        "idinvalid"
      );
    }

    if (!powerPlantStisticsRepository.existsById(id)) {
      throw new BadRequestAlertException(
        "Entity not found",
        ENTITY_NAME,
        "idnotfound"
      );
    }

    Optional<PowerPlantStistics> result = powerPlantStisticsRepository
      .findById(powerPlantStistics.getId())
      .map(existingPowerPlantStistics -> {
        if (powerPlantStistics.getName() != null) {
          existingPowerPlantStistics.setName(powerPlantStistics.getName());
        }
        if (powerPlantStistics.getTotalCount() != null) {
          existingPowerPlantStistics.setTotalCount(
            powerPlantStistics.getTotalCount()
          );
        }
        if (powerPlantStistics.getOnlineCount() != null) {
          existingPowerPlantStistics.setOnlineCount(
            powerPlantStistics.getOnlineCount()
          );
        }
        if (powerPlantStistics.getOfflineCount() != null) {
          existingPowerPlantStistics.setOfflineCount(
            powerPlantStistics.getOfflineCount()
          );
        }
        if (powerPlantStistics.getOtherCount() != null) {
          existingPowerPlantStistics.setOtherCount(
            powerPlantStistics.getOtherCount()
          );
        }
        if (powerPlantStistics.getStatisticDate() != null) {
          existingPowerPlantStistics.setStatisticDate(
            powerPlantStistics.getStatisticDate()
          );
        }
        if (powerPlantStistics.getStatisticTime() != null) {
          existingPowerPlantStistics.setStatisticTime(
            powerPlantStistics.getStatisticTime()
          );
        }

        return existingPowerPlantStistics;
      })
      .map(powerPlantStisticsRepository::save);

    return ResponseUtil.wrapOrNotFound(
      result,
      HeaderUtil.createEntityUpdateAlert(
        applicationName,
        true,
        ENTITY_NAME,
        powerPlantStistics.getId().toString()
      )
    );
  }

  /**
   * {@code GET  /power-plant-stistics} : get all the powerPlantStistics.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of powerPlantStistics in body.
   */
  @GetMapping("")
  public List<PowerPlantStistics> getAllPowerPlantStistics() {
    log.debug("REST request to get all PowerPlantStistics");
    return powerPlantStisticsRepository.findAll();
  }

    /**
     * {@code GET  /province-stistics/byDate}
     *
     * @param dateStr  the date of the provinceStistics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provinceStistics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/byDate")
    public List<PowerPlantStistics> getProvinceStisticsByDate(
        @PathVariable("date") String dateStr
    ) {
        log.debug("REST request to get ProvinceStistics By data {}", dateStr);
        LocalDate date = LocalDate.parse(dateStr);
        return powerPlantStisticsRepository.findByDate(date.toString());
    }

  /**
   * {@code GET  /power-plant-stistics/:id} : get the "id" powerPlantStistics.
   *
   * @param id the id of the powerPlantStistics to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the powerPlantStistics, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  public ResponseEntity<PowerPlantStistics> getPowerPlantStistics(
    @PathVariable("id") Long id
  ) {
    log.debug("REST request to get PowerPlantStistics : {}", id);
    Optional<PowerPlantStistics> powerPlantStistics =
      powerPlantStisticsRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(powerPlantStistics);
  }

  /**
   * {@code DELETE  /power-plant-stistics/:id} : delete the "id" powerPlantStistics.
   *
   * @param id the id of the powerPlantStistics to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePowerPlantStistics(
    @PathVariable("id") Long id
  ) {
    log.debug("REST request to delete PowerPlantStistics : {}", id);
    powerPlantStisticsRepository.deleteById(id);
    return ResponseEntity.noContent()
      .headers(
        HeaderUtil.createEntityDeletionAlert(
          applicationName,
          true,
          ENTITY_NAME,
          id.toString()
        )
      )
      .build();
  }
}

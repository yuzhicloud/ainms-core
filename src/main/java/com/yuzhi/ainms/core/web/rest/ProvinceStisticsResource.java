package com.yuzhi.ainms.core.web.rest;

import com.yuzhi.ainms.core.domain.ProvinceStistics;
import com.yuzhi.ainms.core.repository.ProvinceStisticsRepository;
import com.yuzhi.ainms.core.service.AccessPointService;
import com.yuzhi.ainms.core.service.dto.PowerPlantAPStatisticsDTO;
import com.yuzhi.ainms.core.service.dto.ProvinceAPStatisticsDTO;
import com.yuzhi.ainms.core.web.rest.errors.BadRequestAlertException;
import com.yuzhi.ainms.nce.NCEAPService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yuzhi.ainms.core.domain.ProvinceStistics}.
 */
@RestController
@RequestMapping("/api/province-stistics")
@Transactional
public class ProvinceStisticsResource {

    private final Logger log = LoggerFactory.getLogger(
        ProvinceStisticsResource.class
    );

    private static final String ENTITY_NAME = "provinceStistics";

    private final AccessPointService accessPointService;
    private final NCEAPService nceapService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvinceStisticsRepository provinceStisticsRepository;

    public ProvinceStisticsResource(
        ProvinceStisticsRepository provinceStisticsRepository,
        AccessPointService accessPointService,
        NCEAPService nceapService
    ) {
        this.accessPointService = accessPointService;
        this.provinceStisticsRepository = provinceStisticsRepository;
        this.nceapService = nceapService;
    }

    /**
     * {@code POST  /province-stistics} : Create a new provinceStistics.
     *
     * @param provinceStistics the provinceStistics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provinceStistics, or with status {@code 400 (Bad Request)} if the provinceStistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProvinceStistics> createProvinceStistics(
        @Valid @RequestBody ProvinceStistics provinceStistics
    ) throws URISyntaxException {
        log.debug("REST request to save ProvinceStistics : {}", provinceStistics);
        if (provinceStistics.getId() != null) {
            throw new BadRequestAlertException(
                "A new provinceStistics cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        provinceStistics = provinceStisticsRepository.save(provinceStistics);
        return ResponseEntity.created(
                new URI("/api/province-stistics/" + provinceStistics.getId())
            )
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    provinceStistics.getId().toString()
                )
            )
            .body(provinceStistics);
    }

    /**
     * {@code PUT  /province-stistics/:id} : Updates an existing provinceStistics.
     *
     * @param id               the id of the provinceStistics to save.
     * @param provinceStistics the provinceStistics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinceStistics,
     * or with status {@code 400 (Bad Request)} if the provinceStistics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provinceStistics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProvinceStistics> updateProvinceStistics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProvinceStistics provinceStistics
    ) throws URISyntaxException {
        log.debug(
            "REST request to update ProvinceStistics : {}, {}",
            id,
            provinceStistics
        );
        if (provinceStistics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provinceStistics.getId())) {
            throw new BadRequestAlertException(
                "Invalid ID",
                ENTITY_NAME,
                "idinvalid"
            );
        }

        if (!provinceStisticsRepository.existsById(id)) {
            throw new BadRequestAlertException(
                "Entity not found",
                ENTITY_NAME,
                "idnotfound"
            );
        }

        provinceStistics = provinceStisticsRepository.save(provinceStistics);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    provinceStistics.getId().toString()
                )
            )
            .body(provinceStistics);
    }

    /**
     * {@code PATCH  /province-stistics/:id} : Partial updates given fields of an existing provinceStistics, field will ignore if it is null
     *
     * @param id               the id of the provinceStistics to save.
     * @param provinceStistics the provinceStistics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provinceStistics,
     * or with status {@code 400 (Bad Request)} if the provinceStistics is not valid,
     * or with status {@code 404 (Not Found)} if the provinceStistics is not found,
     * or with status {@code 500 (Internal Server Error)} if the provinceStistics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/{id}",
        consumes = {"application/json", "application/merge-patch+json"}
    )
    public ResponseEntity<ProvinceStistics> partialUpdateProvinceStistics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProvinceStistics provinceStistics
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update ProvinceStistics partially : {}, {}",
            id,
            provinceStistics
        );
        if (provinceStistics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provinceStistics.getId())) {
            throw new BadRequestAlertException(
                "Invalid ID",
                ENTITY_NAME,
                "idinvalid"
            );
        }

        if (!provinceStisticsRepository.existsById(id)) {
            throw new BadRequestAlertException(
                "Entity not found",
                ENTITY_NAME,
                "idnotfound"
            );
        }

        Optional<ProvinceStistics> result = provinceStisticsRepository
            .findById(provinceStistics.getId())
            .map(existingProvinceStistics -> {
                if (provinceStistics.getName() != null) {
                    existingProvinceStistics.setName(provinceStistics.getName());
                }
                if (provinceStistics.getTotalCount() != null) {
                    existingProvinceStistics.setTotalCount(
                        provinceStistics.getTotalCount()
                    );
                }
                if (provinceStistics.getOnlineCount() != null) {
                    existingProvinceStistics.setOnlineCount(
                        provinceStistics.getOnlineCount()
                    );
                }
                if (provinceStistics.getOfflineCount() != null) {
                    existingProvinceStistics.setOfflineCount(
                        provinceStistics.getOfflineCount()
                    );
                }
                if (provinceStistics.getOtherCount() != null) {
                    existingProvinceStistics.setOtherCount(
                        provinceStistics.getOtherCount()
                    );
                }
                if (provinceStistics.getStatisticDate() != null) {
                    existingProvinceStistics.setStatisticDate(
                        provinceStistics.getStatisticDate()
                    );
                }
                if (provinceStistics.getStatisticTime() != null) {
                    existingProvinceStistics.setStatisticTime(
                        provinceStistics.getStatisticTime()
                    );
                }

                return existingProvinceStistics;
            })
            .map(provinceStisticsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                true,
                ENTITY_NAME,
                provinceStistics.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /province-stistics} : get all the provinceStistics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provinceStistics in body.
     */
    @GetMapping("")
    public List<ProvinceStistics> getAllProvinceStistics() {
        log.debug("REST request to get all ProvinceStistics");
        return provinceStisticsRepository.findAll();
    }

    /**
     * {@code GET  /province-stistics/:id} : get the "id" provinceStistics.
     *
     * @param id the id of the provinceStistics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provinceStistics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProvinceStistics> getProvinceStistics(
        @PathVariable("id") Long id
    ) {
        log.debug("REST request to get ProvinceStistics : {}", id);
        Optional<ProvinceStistics> provinceStistics =
            provinceStisticsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(provinceStistics);
    }

    /**
     * {@code GET  /province-stistics/byDate}
     *
     * @param dateStr  the date of the provinceStistics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provinceStistics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/byDate/{dateStr}")
    public ResponseEntity<Page<ProvinceStistics>> getProvinceStatisticsByDate(
        @PathVariable("dateStr") String dateStr,
        @PageableDefault(size = 30) Pageable pageable
    ) {
        log.debug("REST request to get ProvinceStistics By data {}", dateStr);
        LocalDate date = LocalDate.parse(dateStr);
        log.debug("= get province statistics by LocalDate is: {}", dateStr);
        Page<ProvinceStistics> page = provinceStisticsRepository.findByDate(date, pageable);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code DELETE  /province-stistics/:id} : delete the "id" provinceStistics.
     *
     * @param id the id of the provinceStistics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvinceStistics(
        @PathVariable("id") Long id
    ) {
        log.debug("REST request to delete ProvinceStistics : {}", id);
        provinceStisticsRepository.deleteById(id);
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

    /**
    * handle NEC AP statistics
    */
    @GetMapping("/nce-apstatistics")
    public void getNECAPStatistics() {
        log.debug("REST request to get NEC AP statistics");
        try {
            nceapService.syncAllAccessPoints();
        }catch(Exception e){
            log.error(e.getMessage());
        }
        List<ProvinceAPStatisticsDTO> countsProvince = accessPointService.updateAPStatisticsByProvince();
        List<PowerPlantAPStatisticsDTO> countsPowerPlant = accessPointService.updateAPStatisticsByPowerPlant();
        log.debug("REST 1 request to get NEC AP statistics: {}", countsProvince);
        log.debug("REST 2 request to get NEC AP statistics: {}", countsPowerPlant);
    }
}

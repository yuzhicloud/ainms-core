package com.yuzhi.ainms.core.service;

import com.yuzhi.ainms.core.domain.Province;
import com.yuzhi.ainms.core.repository.ProvinceRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yuzhi.ainms.core.domain.Province}.
 */
@Service
@Transactional
public class ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceService.class);

    private final ProvinceRepository provinceRepository;

    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    /**
     * Save a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    public Province save(Province province) {
        log.debug("Request to save Province : {}", province);
        return provinceRepository.save(province);
    }

    /**
     * Update a province.
     *
     * @param province the entity to save.
     * @return the persisted entity.
     */
    public Province update(Province province) {
        log.debug("Request to update Province : {}", province);
        return provinceRepository.save(province);
    }

    /**
     * Partially update a province.
     *
     * @param province the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Province> partialUpdate(Province province) {
        log.debug("Request to partially update Province : {}", province);

        return provinceRepository
            .findById(province.getId())
            .map(existingProvince -> {
                if (province.getProvinceCode() != null) {
                    existingProvince.setProvinceCode(province.getProvinceCode());
                }
                if (province.getProvinceName() != null) {
                    existingProvince.setProvinceName(province.getProvinceName());
                }

                return existingProvince;
            })
            .map(provinceRepository::save);
    }

    /**
     * Get all the provinces.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Province> findAll() {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll();
    }

    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Province> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id);
    }

    /**
     * Delete the province by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}

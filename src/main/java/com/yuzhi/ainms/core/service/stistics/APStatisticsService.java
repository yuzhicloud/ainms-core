package com.yuzhi.ainms.core.service.stistics;

import com.yuzhi.ainms.core.repository.PowerPlantStisticsRepository;
import com.yuzhi.ainms.core.repository.ProvinceStisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class APStatisticsService {

    private final Logger log = LoggerFactory.getLogger(APStatisticsService.class);

    private final ProvinceStisticsRepository provinceStisticsRepository;

    private final PowerPlantStisticsRepository powerPlantStisticsRepository;

    public APStatisticsService(ProvinceStisticsRepository provinceStisticsRepository, PowerPlantStisticsRepository powerPlantStisticsRepository) {
        this.provinceStisticsRepository = provinceStisticsRepository;
        this.powerPlantStisticsRepository = powerPlantStisticsRepository;
    }

    /**
     * Get AP statistics by province with Date
     */
    public void getAPStatisticsByProvince(String date) {
        log.info("APStatisticsService.getAPStatisticsByProvince() with date called");
    }

    /**
     * Get AP statistics by power plant with Date
     */
    public void getAPStatisticsByPowerPlant(String date) {
        log.info("APStatisticsService.getAPStatisticsByPowerPlant() with date called");
    }

}

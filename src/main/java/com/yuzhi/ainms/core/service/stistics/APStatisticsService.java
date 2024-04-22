package com.yuzhi.ainms.core.service.stistics;

import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import com.yuzhi.ainms.core.domain.ProvinceStistics;
import com.yuzhi.ainms.core.repository.PowerPlantStisticsRepository;
import com.yuzhi.ainms.core.repository.ProvinceStisticsRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    /**
     *
     * @return
     * @throws IOException
     */
    public Path createCsvFileByProvince() throws IOException {
        List<ProvinceStistics> records = provinceStisticsRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "statisticsByProvince_" + timestamp + ".csv";

        Path file = Paths.get(ResourceUtils.getFile("classpath:").getPath(), fileName);
        FileWriter out = new FileWriter(file.toString());

        // try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL
            .withHeader("ID", "Province Name", "Total Count", "Online Count", "Offline Count", "Other Count", "Statistic Date", "Statistic Time", "Rate"))) {
            for (ProvinceStistics record : records) {
                double rate = record.getTotalCount() > 0 ? (double) record.getOnlineCount() / record.getTotalCount() : 0;
                printer.printRecord(record.getId(), record.getName(), record.getTotalCount(), record.getOnlineCount(), record.getOfflineCount(),
                    record.getOtherCount(), record.getStatisticDate(), record.getStatisticTime(), rate);
            }
        }
        return file;
    }


    /**
     *
     * @return
     * @throws IOException
     */
    public Path createCsvFileByPowerPlant() throws IOException {
        List<PowerPlantStistics> records = powerPlantStisticsRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "statisticsByProvince_" + timestamp + ".csv";

        Path file = Paths.get(ResourceUtils.getFile("classpath:").getPath(), fileName);
        FileWriter out = new FileWriter(file.toString());

        // try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL
            .withHeader("ID", "Province Name", "Total Count", "Online Count", "Offline Count", "Other Count", "Statistic Date", "Statistic Time", "Rate"))) {
            for (PowerPlantStistics record : records) {
                double rate = record.getTotalCount() > 0 ? (double) record.getOnlineCount() / record.getTotalCount() : 0;
                printer.printRecord(record.getId(), record.getName(), record.getTotalCount(), record.getOnlineCount(), record.getOfflineCount(),
                    record.getOtherCount(), record.getStatisticDate(), record.getStatisticTime(), rate);
            }
        }
        return file;
    }
}

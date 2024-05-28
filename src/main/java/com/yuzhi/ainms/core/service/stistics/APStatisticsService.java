package com.yuzhi.ainms.core.service.stistics;

import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import com.yuzhi.ainms.core.domain.ProvinceStistics;
import com.yuzhi.ainms.core.repository.PowerPlantStisticsRepository;
import com.yuzhi.ainms.core.repository.ProvinceStisticsRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
    public Path createCsvFileByProvince(LocalDate start, LocalDate end) throws IOException {
        List<ProvinceStistics> records = provinceStisticsRepository.findAllByStatisticDateBetween(start, end);
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "statisticsByProvince_" + timestamp + ".csv";

        Path file = Paths.get(ResourceUtils.getFile("classpath:").getPath(), fileName);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file.toString()), StandardCharsets.UTF_8);
        out.write('\ufeff');

        String HEADERS = "ID,Province Name,Total Count,Online Count,Offline Count,Other Count,Statistic Date,Statistic Time,Rate";
        CSVFormat csvFormat = CSVFormat.EXCEL.builder()
            .setHeader(HEADERS)
            .build();

        try (CSVPrinter printer = new CSVPrinter(out, csvFormat)){
            for (ProvinceStistics record : records) {
                double rate = record.getTotalCount() > 0 ? (double) record.getOnlineCount() / record.getTotalCount() : 0;
                String formattedRate = String.format("%.2f", rate);
                String name = new String(record.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                printer.printRecord(record.getId(), name, record.getTotalCount(), record.getOnlineCount(), record.getOfflineCount(),
                    record.getOtherCount(), record.getStatisticDate(), record.getStatisticTime(), formattedRate);
            }
        }
        return file;
    }


    /**
     *
     * @return
     * @throws IOException
     */
    public Path createCsvFileByPowerPlant(
        LocalDate start, LocalDate end
    ) throws IOException {
        List<PowerPlantStistics> records = powerPlantStisticsRepository.findAllByStatisticDateBetween(start, end);
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "statisticsByProvince_" + timestamp + ".csv";

        Path file = Paths.get(ResourceUtils.getFile("classpath:").getPath(), fileName);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file.toString()), StandardCharsets.UTF_8);
        out.write('\ufeff');
        String HEADERS = "ID,PowerPlant Name,Total Count,Online Count,Offline Count,Other Count,Statistic Date,Statistic Time,Rate";
        CSVFormat csvFormat = CSVFormat.EXCEL.builder()
            .setHeader(HEADERS)
            .build();

        try (CSVPrinter printer = new CSVPrinter(out, csvFormat)){
            for (PowerPlantStistics record : records) {
                double rate = record.getTotalCount() > 0 ? (double) record.getOnlineCount() / record.getTotalCount() : 0;
                String formattedRate = String.format("%.2f", rate);
                String name = new String(record.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                printer.printRecord(record.getId(), name, record.getTotalCount(), record.getOnlineCount(), record.getOfflineCount(),
                    record.getOtherCount(), record.getStatisticDate(), record.getStatisticTime(), formattedRate);
            }
        }
        return file;
    }
}

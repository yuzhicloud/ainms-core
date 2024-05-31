package com.yuzhi.ainms.core.service;

import com.yuzhi.ainms.core.domain.*;
import com.yuzhi.ainms.core.repository.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.yuzhi.ainms.core.service.dto.PowerPlantAPStatisticsDTO;
import com.yuzhi.ainms.core.service.dto.PowerPlantWithProvinceDTO;
import com.yuzhi.ainms.core.service.dto.ProvinceAPStatisticsDTO;
import com.yuzhi.ainms.core.service.dto.ProvinceAccessPointCountDTO;
import com.yuzhi.ainms.core.service.dto.NCEAccessPointDTO;
import com.yuzhi.ainms.core.config.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import static java.time.LocalTime.now;

/**
 * Service Implementation for managing {@link com.yuzhi.ainms.core.domain.AccessPoint}.
 */
@Service
@Transactional
public class AccessPointService {

    private final Logger log = LoggerFactory.getLogger(AccessPointService.class);

    private final AccessPointRepository accessPointRepository;
    private final PowerPlantRepository powerPlantRepository;
    private final AccessPointGroupRepository accessPointGroupRepository;
    private final PowerPlantStisticsRepository powerPlantStisticsRepository;
    private final ProvinceStisticsRepository provinceStisticsRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public AccessPointService(AccessPointRepository accessPointRepository,
                                PowerPlantRepository powerPlantRepository,
                                AccessPointGroupRepository accessPointGroupRepository,
                                PowerPlantStisticsRepository powerPlantStisticsRepository,
                                ProvinceStisticsRepository provinceStisticsRepository,
                                SimpMessagingTemplate simpMessagingTemplate
                            ) {
        this.accessPointRepository = accessPointRepository;
        this.accessPointGroupRepository = accessPointGroupRepository;
        this.powerPlantRepository = powerPlantRepository;
        this.provinceStisticsRepository = provinceStisticsRepository;
        this.powerPlantStisticsRepository = powerPlantStisticsRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    /**
     * Save a accessPoint.
     *
     * @param accessPoint the entity to save.
     * @return the persisted entity.
     */
    public AccessPoint save(AccessPoint accessPoint) {
        log.debug("Request to save AccessPoint : {}", accessPoint);
        return accessPointRepository.save(accessPoint);
    }

    /**
     * Update a accessPoint.
     *
     * @param accessPoint the entity to save.
     * @return the persisted entity.
     */
    public AccessPoint update(AccessPoint accessPoint) {
        log.debug("Request to update AccessPoint : {}", accessPoint);
        return accessPointRepository.save(accessPoint);
    }

    /**
     * Partially update a accessPoint.
     *
     * @param accessPoint the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AccessPoint> partialUpdate(AccessPoint accessPoint) {
        log.debug("Request to partially update AccessPoint : {}", accessPoint);

        return accessPointRepository
            .findById(accessPoint.getId())
            .map(existingAccessPoint -> {
                if (accessPoint.getNedn() != null) {
                    existingAccessPoint.setNedn(accessPoint.getNedn());
                }
                if (accessPoint.getNeid() != null) {
                    existingAccessPoint.setNeid(accessPoint.getNeid());
                }
                if (accessPoint.getAliasname() != null) {
                    existingAccessPoint.setAliasname(accessPoint.getAliasname());
                }
                if (accessPoint.getNename() != null) {
                    existingAccessPoint.setNename(accessPoint.getNename());
                }
                if (accessPoint.getNecategory() != null) {
                    existingAccessPoint.setNecategory(accessPoint.getNecategory());
                }
                if (accessPoint.getNetype() != null) {
                    existingAccessPoint.setNetype(accessPoint.getNetype());
                }
                if (accessPoint.getNevendorname() != null) {
                    existingAccessPoint.setNevendorname(accessPoint.getNevendorname());
                }
                if (accessPoint.getNeesn() != null) {
                    existingAccessPoint.setNeesn(accessPoint.getNeesn());
                }
                if (accessPoint.getNeip() != null) {
                    existingAccessPoint.setNeip(accessPoint.getNeip());
                }
                if (accessPoint.getNemac() != null) {
                    existingAccessPoint.setNemac(accessPoint.getNemac());
                }
                if (accessPoint.getVersion() != null) {
                    existingAccessPoint.setVersion(accessPoint.getVersion());
                }
                if (accessPoint.getNestate() != null) {
                    existingAccessPoint.setNestate(accessPoint.getNestate());
                }
                if (accessPoint.getCreatetime() != null) {
                    existingAccessPoint.setCreatetime(accessPoint.getCreatetime());
                }
                if (accessPoint.getNeiptype() != null) {
                    existingAccessPoint.setNeiptype(accessPoint.getNeiptype());
                }
                if (accessPoint.getSubnet() != null) {
                    existingAccessPoint.setSubnet(accessPoint.getSubnet());
                }
                if (accessPoint.getNeosversion() != null) {
                    existingAccessPoint.setNeosversion(accessPoint.getNeosversion());
                }

                return existingAccessPoint;
            })
            .map(accessPointRepository::save);
    }

    /**
     * Get all the accessPoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AccessPoint> findAll( Pageable pageable) {
        log.debug("Request to get all AccessPoints");
        return accessPointRepository.findAll(pageable);
    }

    /**
     * Get one accessPoint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AccessPoint> findOne(Long id) {
        log.debug("Request to get AccessPoint : {}", id);
        return accessPointRepository.findById(id);
    }

    /**
     * Delete the accessPoint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AccessPoint : {}", id);
        accessPointRepository.deleteById(id);
    }

    /**
     * 根据省份ID获取所有相关的AccessPoint。
     *
     * @param provinceId 省份的ID。
     * @return 相关的AccessPoint列表。
     */
    public Page<AccessPoint> findAllAccessPointsByProvinceId(Long provinceId,  Pageable  pageable){
        // 一次性获取省份下的所有PowerPlant的ID
        log.debug("==Request1 to findAllAccessPointsByProvinceId, provinceId: {}", provinceId);
        List<Long> powerPlantIds = powerPlantRepository.findAllByProvinceId(provinceId).stream()
            .map(PowerPlantWithProvinceDTO::getPowerPlantId)
            .collect(Collectors.toList());
        log.debug("==Request2 to findAllAccessPointsByProvinceId, powerPlantIds: {}", powerPlantIds.toString());
        // 使用IN查询一次性获取所有这些PowerPlant下的AccessPointGroup的ID
        List<Long> accessPointGroupIds = accessPointGroupRepository.findByPowerPlantIdIn(powerPlantIds).stream()
            .map(AccessPointGroup::getId)
            .collect(Collectors.toList());

        log.debug("==Request3 to findAllAccessPointsByProvinceId, accessPointGroupIds: {}", accessPointGroupIds.toString());
        Page<AccessPoint> accessPoints = accessPointRepository.findByGroupIdsIn(accessPointGroupIds, pageable);
        return accessPoints;
    }

    public Page<AccessPoint> findAllAccessPointsByPlantId(Long plantId,  Pageable  pageable){
        // 一次性获取省份下的所有PowerPlant的ID
       // log.debug("==Request1 to findAllAccessPointsByProvinceId, provinceId: {}", provinceId);
       // List<Long> powerPlantIds = powerPlantRepository.findAllByProvinceId(provinceId).stream()
       //     .map(PowerPlantWithProvinceDTO::getPowerPlantId)
       //     .collect(Collectors.toList());
        List<Long> powerPlantIds = new ArrayList<>();
        powerPlantIds.add(plantId);

        log.debug("==Request2 to findAllAccessPointsByProvinceId, powerPlantIds: {}", powerPlantIds.toString());
        // 使用IN查询一次性获取所有这些PowerPlant下的AccessPointGroup的ID
        List<Long> accessPointGroupIds = accessPointGroupRepository.findByPowerPlantIdIn(powerPlantIds).stream()
            .map(AccessPointGroup::getId)
            .collect(Collectors.toList());

        log.debug("==Request3 to findAllAccessPointsByProvinceId, accessPointGroupIds: {}", accessPointGroupIds.toString());
        Page<AccessPoint> accessPoints = accessPointRepository.findByGroupIdsIn(accessPointGroupIds, pageable);
        return accessPoints;
    }

    public Page<AccessPoint> findAllAccessPointsByPlantName(String plantName, Long provinceid, Long powerplantid,  Pageable  pageable){
        // 一次性获取省份下的所有PowerPlant的ID
        // log.debug("==Request1 to findAllAccessPointsByProvinceId, provinceId: {}", provinceId);
        List<PowerPlant> powerPlants = new ArrayList<>();
        if(provinceid > 0){
            powerPlants = powerPlantRepository.findAllByProvinceIdAndPowerPlantNameContaining(provinceid, plantName);
        }else if(powerplantid > 0){
            //List<PowerPlant> powerPlantS = new ArrayList<>();
            powerPlants = powerPlantRepository.findAllByIdAndPowerPlantNameContaining(powerplantid, plantName);
        }else{
            powerPlants = powerPlantRepository.findAllByPowerPlantNameContaining(plantName);
        }

        //List<PowerPlant> powerPlants = powerPlantRepository.findAllByPowerPlantNameContaining(plantName);

         List<Long> powerPlantIds = powerPlants.stream().map(PowerPlant::getId)
                                    .collect(Collectors.toList());
        //List<Long> powerPlantIds = new ArrayList<>();
       // powerPlantIds.add(plantId);

        log.debug("==Request2 to findAllAccessPointsByProvinceId, powerPlantIds: {}", powerPlantIds.toString());
        // 使用IN查询一次性获取所有这些PowerPlant下的AccessPointGroup的ID
        List<Long> accessPointGroupIds = accessPointGroupRepository.findByPowerPlantIdIn(powerPlantIds).stream()
            .map(AccessPointGroup::getId)
            .collect(Collectors.toList());

        log.debug("==Request3 to findAllAccessPointsByProvinceId, accessPointGroupIds: {}", accessPointGroupIds.toString());
        Page<AccessPoint> accessPoints = accessPointRepository.findByGroupIdsIn(accessPointGroupIds, pageable);
        return accessPoints;
    }

    public Path createCsvFileByPowerPlant(
        String key
    ) throws IOException {
        Pageable pageble = Pageable.unpaged();
        Page<AccessPoint> records = this.findAllAccessPointsByPlantName(key, 0L, 0L, pageble);
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = "accessPoints_" + timestamp + ".csv";

        Path file = Paths.get(ResourceUtils.getFile("classpath:").getPath(), fileName);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file.toString()), StandardCharsets.UTF_8);
        out.write('\ufeff');
        String HEADERS = "ID, AP Name, AP Type, AP State, AP Group Name";
        CSVFormat csvFormat = CSVFormat.EXCEL.builder()
            .setHeader(HEADERS)
            .build();

        try (CSVPrinter printer = new CSVPrinter(out, csvFormat)) {
            for (AccessPoint record : records.getContent()) {
               // double rate = record.getTotalCount() > 0 ? (double) record.getOnlineCount() / record.getTotalCount() : 0;
                String state = (record.getNestate() == 11 || record.getNestate() == 7) ? "在线":"离线";
              //  String name = new String(record.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                printer.printRecord(record.getId(), record.getNename(), record.getNetype(), state, record.getGroup().getName());
            }
        }
        return file;
    }

        /*
        * 根据省份来获取每个省份的AP数量
        *
        */
    public List<ProvinceAccessPointCountDTO> getAccessPointCountsByProvince() {
        return powerPlantRepository.countAccessPointsByProvince();
    }

    /**
    * 根据省份来获取每个省份的AP统计情况
    */
    public List<ProvinceAPStatisticsDTO> updateAPStatisticsByProvince() {
        log.debug("==getAPStatisticsByProvince");
        List<ProvinceAPStatisticsDTO> result = accessPointRepository.apStatisticsByProvince();
        // save to database
        // change class in result to ProvinceStistics and save
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDate localDate = LocalDate.now(zoneId);
        LocalTime localTime = LocalTime.now(zoneId);
        log.debug("==getAPStatisticsByProvince, localtime: {}" + localTime.toString());

        result.forEach(dto -> {
            ProvinceStistics provinceStistics = new ProvinceStistics();
            provinceStistics.setName(dto.getProvinceName());
            provinceStistics.setTotalCount(dto.getTotalAPs());
            provinceStistics.setOnlineCount(dto.getStandByAPCount());
            provinceStistics.setOfflineCount(dto.getOfflineAPCount());
            provinceStistics.setOtherCount(dto.getOtherAPCount());
            provinceStistics.setStatisticDate(localDate);
            provinceStistics.setStatisticTime(localTime);
            provinceStisticsRepository.save(provinceStistics);
        });

        // 发送更新通知到前端
        notifyFrontend("updateAPs,successful");
        return result;
    }

    /**
    * 根据场站来获取每个场站的AP统计情况
    */
    public List<PowerPlantAPStatisticsDTO> updateAPStatisticsByPowerPlant() {
        List<PowerPlantAPStatisticsDTO> result = accessPointRepository.apStatisticsByPowerPlant();
        // save to database
        // change class in result to PowerPlantStistics and save
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        LocalDate localDate = LocalDate.now(zoneId);
        LocalTime localTime = LocalTime.now(zoneId);
        result.forEach(dto -> {
            PowerPlantStistics powerPlantStistics = new PowerPlantStistics();
            powerPlantStistics.setName(dto.getPowerPlantName());
            powerPlantStistics.setTotalCount(dto.getTotalAPs());
            powerPlantStistics.setOnlineCount(dto.getStandByAPCount());
            powerPlantStistics.setOfflineCount(dto.getOfflineAPCount());
            powerPlantStistics.setOtherCount(dto.getOtherAPCount());
            powerPlantStistics.setStatisticDate(localDate);
            powerPlantStistics.setStatisticTime(localTime);
            powerPlantStisticsRepository.save(powerPlantStistics);
        });
        notifyFrontend("updateAPs,successful");
        return result;
    }

    /**
    * 根据从NCE获取的AP的值，更新数据库中的AP状态
    *  网管状态为11的AP，如果NCE中的状态不为0，则更新为4-offline
    */
    @Transactional
    public void updateAccessPoints(List<NCEAccessPointDTO> dtos) {
        log.debug("==Apservice updateAccessPoints, accessPoints: {}", dtos.toString());
        dtos.forEach(dto -> {
            AccessPoint existingAccessPoint = accessPointRepository.findByEsn(dto.getApSn());
            if (existingAccessPoint != null && dto.getApStatus() != Constants.NCE_AP_STATUS_ACTIVE
                && existingAccessPoint.getNestate().equals(Constants.NMS_AP_STATUS_ACTIVE)) {
                log.debug("==Apservice new state: 4-offline, existingAP: {}", existingAccessPoint.toString());
                existingAccessPoint.setNestate(Constants.NMS_AP_STATUS_OFFLINE);
                accessPointRepository.save(existingAccessPoint);
            }
        });
    }

    private void notifyFrontend(Object message) {
        this.simpMessagingTemplate.convertAndSend("/topic/update", message);
    }

}

package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProvinceStistics.
 */
@Entity
@Table(name = "province_stistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProvinceStistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "total_count")
    private Long totalCount;

    @Column(name = "online_count")
    private Long onlineCount;

    @Column(name = "offline_count")
    private Long offlineCount;

    @Column(name = "other_count")
    private Long otherCount;

    @Column(name = "statistic_date",columnDefinition = "DATE")
    private LocalDate statisticDate;

    @Column(name = "statistic_time",columnDefinition = "DATETIME")
    private LocalTime statisticTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
    @JsonIgnoreProperties(value = {"province"}, allowSetters = true)
    private Set<PowerPlantStistics> powerplants = new HashSet<>();

    public Long getId() {
        return this.id;
    }

    public ProvinceStistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ProvinceStistics name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public ProvinceStistics totalCount(Long totalCount) {
        this.setTotalCount(totalCount);
        return this;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getOnlineCount() {
        return this.onlineCount;
    }

    public ProvinceStistics onlineCount(Long onlineCount) {
        this.setOnlineCount(onlineCount);
        return this;
    }

    public void setOnlineCount(Long onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Long getOfflineCount() {
        return this.offlineCount;
    }

    public ProvinceStistics offlineCount(Long offlineCount) {
        this.setOfflineCount(offlineCount);
        return this;
    }

    public void setOfflineCount(Long offlineCount) {
        this.offlineCount = offlineCount;
    }

    public Long getOtherCount() {
        return this.otherCount;
    }

    public ProvinceStistics otherCount(Long otherCount) {
        this.setOtherCount(otherCount);
        return this;
    }

    public void setOtherCount(Long otherCount) {
        this.otherCount = otherCount;
    }

    public LocalDate getStatisticDate() {
        return this.statisticDate;
    }

    public ProvinceStistics statisticDate(LocalDate statisticDate) {
        this.setStatisticDate(statisticDate);
        return this;
    }

    public void setStatisticDate(LocalDate statisticDate) {
        this.statisticDate = statisticDate;
    }

    public LocalTime getStatisticTime() {
        // 转换为Asia/Shanghai时区的时间
        // 注意：这需要原始时间存储为UTC
        return statisticTime.atDate(LocalDate.of(1970, 1, 1)) // 需要一个日期来创建LocalDateTime
            .atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.of("Asia/Shanghai"))
            .toLocalTime();
    }

    public void setStatisticTime(LocalTime statisticTime) {
        // 假定输入的时间是Asia/Shanghai时区的时间，转换为UTC时间存储
        this.statisticTime = statisticTime.atDate(LocalDate.of(1970, 1, 1)) // 同样需要一个日期来创建LocalDateTime
            .atZone(ZoneId.of("Asia/Shanghai"))
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toLocalTime();
    }

    public Set<PowerPlantStistics> getPowerplants() {
        return this.powerplants;
    }

    public void setPowerplants(Set<PowerPlantStistics> powerPlantStistics) {
        if (this.powerplants != null) {
            this.powerplants.forEach(i -> i.setProvince(null));
        }
        if (powerPlantStistics != null) {
            powerPlantStistics.forEach(i -> i.setProvince(this));
        }
        this.powerplants = powerPlantStistics;
    }

    public ProvinceStistics stations(Set<PowerPlantStistics> powerPlantStistics) {
        this.setPowerplants(powerPlantStistics);
        return this;
    }

    public ProvinceStistics addStation(PowerPlantStistics powerPlantStistics) {
        this.powerplants.add(powerPlantStistics);
        powerPlantStistics.setProvince(this);
        return this;
    }

    public ProvinceStistics removeStation(PowerPlantStistics powerPlantStistics) {
        this.powerplants.remove(powerPlantStistics);
        powerPlantStistics.setProvince(null);
        return this;
    }

// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProvinceStistics)) {
            return false;
        }
        return getId() != null && getId().equals(((ProvinceStistics) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}

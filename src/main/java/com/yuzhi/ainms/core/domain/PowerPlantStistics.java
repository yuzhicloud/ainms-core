package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.*;

/**
 * A PowerPlantStistics.
 */
@Entity
@Table(name = "power_plant_stistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PowerPlantStistics implements Serializable {

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

    @Column(name = "statistic_date", columnDefinition = "DATE")
    private LocalDate statisticDate;

    @Column(name = "statistic_time", columnDefinition = "DATETIME")
    private LocalTime statisticTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"stations", "country"}, allowSetters = true)
    private ProvinceStistics province;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PowerPlantStistics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PowerPlantStistics name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public PowerPlantStistics totalCount(Long totalCount) {
        this.setTotalCount(totalCount);
        return this;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getOnlineCount() {
        return this.onlineCount;
    }

    public PowerPlantStistics onlineCount(Long onlineCount) {
        this.setOnlineCount(onlineCount);
        return this;
    }

    public void setOnlineCount(Long onlineCount) {
        this.onlineCount = onlineCount;
    }

    public Long getOfflineCount() {
        return this.offlineCount;
    }

    public PowerPlantStistics offlineCount(Long offlineCount) {
        this.setOfflineCount(offlineCount);
        return this;
    }

    public void setOfflineCount(Long offlineCount) {
        this.offlineCount = offlineCount;
    }

    public Long getOtherCount() {
        return this.otherCount;
    }

    public PowerPlantStistics otherCount(Long otherCount) {
        this.setOtherCount(otherCount);
        return this;
    }

    public void setOtherCount(Long otherCount) {
        this.otherCount = otherCount;
    }

    public LocalDate getStatisticDate() {
        return this.statisticDate;
    }

    public PowerPlantStistics statisticDate(LocalDate statisticDate) {
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

    public ProvinceStistics getProvince() {
        return this.province;
    }

    public void setProvince(ProvinceStistics provinceStistics) {
        this.province = provinceStistics;
    }

    public PowerPlantStistics province(ProvinceStistics provinceStistics) {
        this.setProvince(provinceStistics);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PowerPlantStistics)) {
            return false;
        }
        return getId() != null && getId().equals(((PowerPlantStistics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PowerPlantStistics{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", totalCount=" + getTotalCount() +
            ", onlineCount=" + getOnlineCount() +
            ", offlineCount=" + getOfflineCount() +
            ", otherCount=" + getOtherCount() +
            ", statisticDate='" + getStatisticDate() + "'" +
            ", statisticTime='" + getStatisticTime() + "'" +
            "}";
    }
}

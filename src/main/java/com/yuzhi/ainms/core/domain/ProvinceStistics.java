package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
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

  @Column(name = "statistic_date")
  private LocalDate statisticDate;

  @Column(name = "statistic_time")
  private Instant statisticTime;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "province")
  @JsonIgnoreProperties(value = { "province" }, allowSetters = true)
  private Set<PowerPlantStistics> stations = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = { "provinces" }, allowSetters = true)
  private CountryStistics country;

  // jhipster-needle-entity-add-field - JHipster will add fields here

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

  public Instant getStatisticTime() {
    return this.statisticTime;
  }

  public ProvinceStistics statisticTime(Instant statisticTime) {
    this.setStatisticTime(statisticTime);
    return this;
  }

  public void setStatisticTime(Instant statisticTime) {
    this.statisticTime = statisticTime;
  }

  public Set<PowerPlantStistics> getStations() {
    return this.stations;
  }

  public void setStations(Set<PowerPlantStistics> powerPlantStistics) {
    if (this.stations != null) {
      this.stations.forEach(i -> i.setProvince(null));
    }
    if (powerPlantStistics != null) {
      powerPlantStistics.forEach(i -> i.setProvince(this));
    }
    this.stations = powerPlantStistics;
  }

  public ProvinceStistics stations(Set<PowerPlantStistics> powerPlantStistics) {
    this.setStations(powerPlantStistics);
    return this;
  }

  public ProvinceStistics addStation(PowerPlantStistics powerPlantStistics) {
    this.stations.add(powerPlantStistics);
    powerPlantStistics.setProvince(this);
    return this;
  }

  public ProvinceStistics removeStation(PowerPlantStistics powerPlantStistics) {
    this.stations.remove(powerPlantStistics);
    powerPlantStistics.setProvince(null);
    return this;
  }

  public CountryStistics getCountry() {
    return this.country;
  }

  public void setCountry(CountryStistics countryStistics) {
    this.country = countryStistics;
  }

  public ProvinceStistics country(CountryStistics countryStistics) {
    this.setCountry(countryStistics);
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
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "ProvinceStistics{" +
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

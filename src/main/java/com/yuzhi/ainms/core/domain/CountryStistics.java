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
 * A CountryStistics.
 */
@Entity
@Table(name = "country_stistics")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryStistics implements Serializable {

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

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
  @JsonIgnoreProperties(value = { "stations", "country" }, allowSetters = true)
  private Set<ProvinceStistics> provinces = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public CountryStistics id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public CountryStistics name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getTotalCount() {
    return this.totalCount;
  }

  public CountryStistics totalCount(Long totalCount) {
    this.setTotalCount(totalCount);
    return this;
  }

  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  public Long getOnlineCount() {
    return this.onlineCount;
  }

  public CountryStistics onlineCount(Long onlineCount) {
    this.setOnlineCount(onlineCount);
    return this;
  }

  public void setOnlineCount(Long onlineCount) {
    this.onlineCount = onlineCount;
  }

  public Long getOfflineCount() {
    return this.offlineCount;
  }

  public CountryStistics offlineCount(Long offlineCount) {
    this.setOfflineCount(offlineCount);
    return this;
  }

  public void setOfflineCount(Long offlineCount) {
    this.offlineCount = offlineCount;
  }

  public Long getOtherCount() {
    return this.otherCount;
  }

  public CountryStistics otherCount(Long otherCount) {
    this.setOtherCount(otherCount);
    return this;
  }

  public void setOtherCount(Long otherCount) {
    this.otherCount = otherCount;
  }

  public LocalDate getStatisticDate() {
    return this.statisticDate;
  }

  public CountryStistics statisticDate(LocalDate statisticDate) {
    this.setStatisticDate(statisticDate);
    return this;
  }

  public void setStatisticDate(LocalDate statisticDate) {
    this.statisticDate = statisticDate;
  }

  public Instant getStatisticTime() {
    return this.statisticTime;
  }

  public CountryStistics statisticTime(Instant statisticTime) {
    this.setStatisticTime(statisticTime);
    return this;
  }

  public void setStatisticTime(Instant statisticTime) {
    this.statisticTime = statisticTime;
  }

  public Set<ProvinceStistics> getProvinces() {
    return this.provinces;
  }

  public void setProvinces(Set<ProvinceStistics> provinceStistics) {
    if (this.provinces != null) {
      this.provinces.forEach(i -> i.setCountry(null));
    }
    if (provinceStistics != null) {
      provinceStistics.forEach(i -> i.setCountry(this));
    }
    this.provinces = provinceStistics;
  }

  public CountryStistics provinces(Set<ProvinceStistics> provinceStistics) {
    this.setProvinces(provinceStistics);
    return this;
  }

  public CountryStistics addProvince(ProvinceStistics provinceStistics) {
    this.provinces.add(provinceStistics);
    provinceStistics.setCountry(this);
    return this;
  }

  public CountryStistics removeProvince(ProvinceStistics provinceStistics) {
    this.provinces.remove(provinceStistics);
    provinceStistics.setCountry(null);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CountryStistics)) {
      return false;
    }
    return getId() != null && getId().equals(((CountryStistics) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CountryStistics{" +
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

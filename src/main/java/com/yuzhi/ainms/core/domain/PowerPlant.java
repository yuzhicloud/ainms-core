package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PowerPlant.
 */
@Entity
@Table(name = "power_plant")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PowerPlant implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "power_plant_name")
  private String powerPlantName;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "powerPlant")
  @JsonIgnoreProperties(
    value = { "accessPoints", "controller", "powerPlant" },
    allowSetters = true
  )
  private Set<AccessPointGroup> accessPointGroups = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(value = { "powerPlants" }, allowSetters = true)
  private Province province;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public PowerPlant id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPowerPlantName() {
    return this.powerPlantName;
  }

  public PowerPlant powerPlantName(String powerPlantName) {
    this.setPowerPlantName(powerPlantName);
    return this;
  }

  public void setPowerPlantName(String powerPlantName) {
    this.powerPlantName = powerPlantName;
  }

  public Set<AccessPointGroup> getAccessPointGroups() {
    return this.accessPointGroups;
  }

  public void setAccessPointGroups(Set<AccessPointGroup> accessPointGroups) {
    if (this.accessPointGroups != null) {
      this.accessPointGroups.forEach(i -> i.setPowerPlant(null));
    }
    if (accessPointGroups != null) {
      accessPointGroups.forEach(i -> i.setPowerPlant(this));
    }
    this.accessPointGroups = accessPointGroups;
  }

  public PowerPlant accessPointGroups(Set<AccessPointGroup> accessPointGroups) {
    this.setAccessPointGroups(accessPointGroups);
    return this;
  }

  public PowerPlant addAccessPointGroup(AccessPointGroup accessPointGroup) {
    this.accessPointGroups.add(accessPointGroup);
    accessPointGroup.setPowerPlant(this);
    return this;
  }

  public PowerPlant removeAccessPointGroup(AccessPointGroup accessPointGroup) {
    this.accessPointGroups.remove(accessPointGroup);
    accessPointGroup.setPowerPlant(null);
    return this;
  }

  public Province getProvince() {
    return this.province;
  }

  public void setProvince(Province province) {
    this.province = province;
  }

  public PowerPlant province(Province province) {
    this.setProvince(province);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PowerPlant)) {
      return false;
    }
    return getId() != null && getId().equals(((PowerPlant) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "PowerPlant{" +
            "id=" + getId() +
            ", powerPlantName='" + getPowerPlantName() + "'" +
            "}";
    }
}

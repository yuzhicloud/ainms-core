package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Province.
 */
@Entity
@Table(name = "province")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "province_code")
    private Long provinceCode;

    @Column(name = "province_name")
    private String provinceName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "powerPlant")
    @JsonIgnoreProperties(value = { "accessPointGroup", "powerPlant" }, allowSetters = true)
    private Set<PowerPlant> provinces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Province id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProvinceCode() {
        return this.provinceCode;
    }

    public Province provinceCode(Long provinceCode) {
        this.setProvinceCode(provinceCode);
        return this;
    }

    public void setProvinceCode(Long provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public Province provinceName(String provinceName) {
        this.setProvinceName(provinceName);
        return this;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Set<PowerPlant> getProvinces() {
        return this.provinces;
    }

    public void setProvinces(Set<PowerPlant> powerPlants) {
        if (this.provinces != null) {
            this.provinces.forEach(i -> i.setPowerPlant(null));
        }
        if (powerPlants != null) {
            powerPlants.forEach(i -> i.setPowerPlant(this));
        }
        this.provinces = powerPlants;
    }

    public Province provinces(Set<PowerPlant> powerPlants) {
        this.setProvinces(powerPlants);
        return this;
    }

    public Province addProvince(PowerPlant powerPlant) {
        this.provinces.add(powerPlant);
        powerPlant.setPowerPlant(this);
        return this;
    }

    public Province removeProvince(PowerPlant powerPlant) {
        this.provinces.remove(powerPlant);
        powerPlant.setPowerPlant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }
        return getId() != null && getId().equals(((Province) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Province{" +
            "id=" + getId() +
            ", provinceCode=" + getProvinceCode() +
            ", provinceName='" + getProvinceName() + "'" +
            "}";
    }
}

package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AccessPointGroup.
 */
@Entity
@Table(name = "access_point_group")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccessPointGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "apg_id")
    private Long apgId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnoreProperties(value = { "accessPointGroup", "powerPlant" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private PowerPlant powerPlant;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_access_point_group__access_point",
        joinColumns = @JoinColumn(name = "access_point_group_id"),
        inverseJoinColumns = @JoinColumn(name = "access_point_id")
    )
    @JsonIgnoreProperties(value = { "controller", "groups" }, allowSetters = true)
    private Set<AccessPoint> accessPoints = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "accessPoints", "accessPointGroups" }, allowSetters = true)
    private AccessController controller;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccessPointGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApgId() {
        return this.apgId;
    }

    public AccessPointGroup apgId(Long apgId) {
        this.setApgId(apgId);
        return this;
    }

    public void setApgId(Long apgId) {
        this.apgId = apgId;
    }

    public String getName() {
        return this.name;
    }

    public AccessPointGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PowerPlant getPowerPlant() {
        return this.powerPlant;
    }

    public void setPowerPlant(PowerPlant powerPlant) {
        this.powerPlant = powerPlant;
    }

    public AccessPointGroup powerPlant(PowerPlant powerPlant) {
        this.setPowerPlant(powerPlant);
        return this;
    }

    public Set<AccessPoint> getAccessPoints() {
        return this.accessPoints;
    }

    public void setAccessPoints(Set<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public AccessPointGroup accessPoints(Set<AccessPoint> accessPoints) {
        this.setAccessPoints(accessPoints);
        return this;
    }

    public AccessPointGroup addAccessPoint(AccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
        return this;
    }

    public AccessPointGroup removeAccessPoint(AccessPoint accessPoint) {
        this.accessPoints.remove(accessPoint);
        return this;
    }

    public AccessController getController() {
        return this.controller;
    }

    public void setController(AccessController accessController) {
        this.controller = accessController;
    }

    public AccessPointGroup controller(AccessController accessController) {
        this.setController(accessController);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccessPointGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((AccessPointGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccessPointGroup{" +
            "id=" + getId() +
            ", apgId=" + getApgId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

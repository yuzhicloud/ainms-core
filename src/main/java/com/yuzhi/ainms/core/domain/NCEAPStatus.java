package com.yuzhi.ainms.core.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A NCEAPStatus.
 */
@Entity
@Table(name = "nceap_status")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NCEAPStatus implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "ap_sn")
  private String apSn;

  @Column(name = "ap_status")
  private Integer apStatus;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Integer getId() {
    return this.id;
  }

  public NCEAPStatus id(Integer id) {
    this.setId(id);
    return this;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getApSn() {
    return this.apSn;
  }

  public NCEAPStatus apSn(String apSn) {
    this.setApSn(apSn);
    return this;
  }

  public void setApSn(String apSn) {
    this.apSn = apSn;
  }

  public Integer getApStatus() {
    return this.apStatus;
  }

  public NCEAPStatus apStatus(Integer apStatus) {
    this.setApStatus(apStatus);
    return this;
  }

  public void setApStatus(Integer apStatus) {
    this.apStatus = apStatus;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NCEAPStatus)) {
      return false;
    }
    return getId() != null && getId().equals(((NCEAPStatus) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "NCEAPStatus{" +
            "id=" + getId() +
            ", apSn='" + getApSn() + "'" +
            ", apStatus=" + getApStatus() +
            "}";
    }
}

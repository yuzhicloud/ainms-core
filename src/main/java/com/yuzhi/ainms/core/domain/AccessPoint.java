package com.yuzhi.ainms.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A AccessPoint.
 */
@Entity
@Table(name = "access_point")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccessPoint implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "nedn")
  private String nedn;

  @Column(name = "neid")
  private Integer neid;

  @Column(name = "aliasname")
  private String aliasname;

  @Column(name = "nename")
  private String nename;

  @Column(name = "necategory")
  private String necategory;

  @Column(name = "netype")
  private String netype;

  @Column(name = "nevendorname")
  private String nevendorname;

  @Column(name = "neesn")
  private String neesn;

  @Column(name = "neip")
  private String neip;

  @Column(name = "nemac")
  private String nemac;

  @Column(name = "version")
  private String version;

  @Column(name = "nestate")
  private Integer nestate;

  @Column(name = "createtime")
  private String createtime;

  @Column(name = "neiptype")
  private Integer neiptype;

  @Column(name = "subnet")
  private String subnet;

  @Column(name = "neosversion")
  private String neosversion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnoreProperties(
    value = { "accessPoints", "controller", "powerPlant" },
    allowSetters = true
  )
  private AccessPointGroup group;

  // jhipster-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public AccessPoint id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNedn() {
    return this.nedn;
  }

  public AccessPoint nedn(String nedn) {
    this.setNedn(nedn);
    return this;
  }

  public void setNedn(String nedn) {
    this.nedn = nedn;
  }

  public Integer getNeid() {
    return this.neid;
  }

  public AccessPoint neid(Integer neid) {
    this.setNeid(neid);
    return this;
  }

  public void setNeid(Integer neid) {
    this.neid = neid;
  }

  public String getAliasname() {
    return this.aliasname;
  }

  public AccessPoint aliasname(String aliasname) {
    this.setAliasname(aliasname);
    return this;
  }

  public void setAliasname(String aliasname) {
    this.aliasname = aliasname;
  }

  public String getNename() {
    return this.nename;
  }

  public AccessPoint nename(String nename) {
    this.setNename(nename);
    return this;
  }

  public void setNename(String nename) {
    this.nename = nename;
  }

  public String getNecategory() {
    return this.necategory;
  }

  public AccessPoint necategory(String necategory) {
    this.setNecategory(necategory);
    return this;
  }

  public void setNecategory(String necategory) {
    this.necategory = necategory;
  }

  public String getNetype() {
    return this.netype;
  }

  public AccessPoint netype(String netype) {
    this.setNetype(netype);
    return this;
  }

  public void setNetype(String netype) {
    this.netype = netype;
  }

  public String getNevendorname() {
    return this.nevendorname;
  }

  public AccessPoint nevendorname(String nevendorname) {
    this.setNevendorname(nevendorname);
    return this;
  }

  public void setNevendorname(String nevendorname) {
    this.nevendorname = nevendorname;
  }

  public String getNeesn() {
    return this.neesn;
  }

  public AccessPoint neesn(String neesn) {
    this.setNeesn(neesn);
    return this;
  }

  public void setNeesn(String neesn) {
    this.neesn = neesn;
  }

  public String getNeip() {
    return this.neip;
  }

  public AccessPoint neip(String neip) {
    this.setNeip(neip);
    return this;
  }

  public void setNeip(String neip) {
    this.neip = neip;
  }

  public String getNemac() {
    return this.nemac;
  }

  public AccessPoint nemac(String nemac) {
    this.setNemac(nemac);
    return this;
  }

  public void setNemac(String nemac) {
    this.nemac = nemac;
  }

  public String getVersion() {
    return this.version;
  }

  public AccessPoint version(String version) {
    this.setVersion(version);
    return this;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Integer getNestate() {
    return this.nestate;
  }

  public AccessPoint nestate(Integer nestate) {
    this.setNestate(nestate);
    return this;
  }

  public void setNestate(Integer nestate) {
    this.nestate = nestate;
  }

  public String getCreatetime() {
    return this.createtime;
  }

  public AccessPoint createtime(String createtime) {
    this.setCreatetime(createtime);
    return this;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }

  public Integer getNeiptype() {
    return this.neiptype;
  }

  public AccessPoint neiptype(Integer neiptype) {
    this.setNeiptype(neiptype);
    return this;
  }

  public void setNeiptype(Integer neiptype) {
    this.neiptype = neiptype;
  }

  public String getSubnet() {
    return this.subnet;
  }

  public AccessPoint subnet(String subnet) {
    this.setSubnet(subnet);
    return this;
  }

  public void setSubnet(String subnet) {
    this.subnet = subnet;
  }

  public String getNeosversion() {
    return this.neosversion;
  }

  public AccessPoint neosversion(String neosversion) {
    this.setNeosversion(neosversion);
    return this;
  }

  public void setNeosversion(String neosversion) {
    this.neosversion = neosversion;
  }

  public AccessPointGroup getGroup() {
    return this.group;
  }

  public void setGroup(AccessPointGroup accessPointGroup) {
    this.group = accessPointGroup;
  }

  public AccessPoint group(AccessPointGroup accessPointGroup) {
    this.setGroup(accessPointGroup);
    return this;
  }

  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AccessPoint)) {
      return false;
    }
    return getId() != null && getId().equals(((AccessPoint) o).getId());
  }

  @Override
  public int hashCode() {
    // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    return getClass().hashCode();
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "AccessPoint{" +
            "id=" + getId() +
            ", nedn='" + getNedn() + "'" +
            ", neid=" + getNeid() +
            ", aliasname='" + getAliasname() + "'" +
            ", nename='" + getNename() + "'" +
            ", necategory='" + getNecategory() + "'" +
            ", netype='" + getNetype() + "'" +
            ", nevendorname='" + getNevendorname() + "'" +
            ", neesn='" + getNeesn() + "'" +
            ", neip='" + getNeip() + "'" +
            ", nemac='" + getNemac() + "'" +
            ", version='" + getVersion() + "'" +
            ", nestate=" + getNestate() +
            ", createtime='" + getCreatetime() + "'" +
            ", neiptype=" + getNeiptype() +
            ", subnet='" + getSubnet() + "'" +
            ", neosversion='" + getNeosversion() + "'" +
            "}";
    }
}

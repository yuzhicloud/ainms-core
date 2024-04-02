package com.yuzhi.ainms.core.service.dto;

import com.yuzhi.ainms.core.domain.AccessController;
import com.yuzhi.ainms.core.domain.AccessPoint;
import com.yuzhi.ainms.core.domain.AccessPointGroup;

public class ApWithAllDTO {
    private AccessPoint accessPoint;
    private AccessController accessController;
    private AccessPointGroup accessPointGroup;

    public ApWithAllDTO(AccessPoint accessPoint, AccessController accessController, AccessPointGroup accessPointGroup) {
        this.accessPoint = accessPoint;
        this.accessController = accessController;
        this.accessPointGroup = accessPointGroup;
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

    public AccessController getAccessController() {
        return accessController;
    }

    public void setAccessController(AccessController accessController) {
        this.accessController = accessController;
    }

    public AccessPointGroup getAccessPointGroup() {
        return accessPointGroup;
    }

    public void setAccessPointGroup(AccessPointGroup accessPointGroup) {
        this.accessPointGroup = accessPointGroup;
    }

    @Override
    public String toString() {
        return "ApWithAlldTO{" +
            "accessPoint=" + accessPoint +
            ", accessController=" + accessController +
            ", accessPointGroup=" + accessPointGroup +
            '}';
    }
}

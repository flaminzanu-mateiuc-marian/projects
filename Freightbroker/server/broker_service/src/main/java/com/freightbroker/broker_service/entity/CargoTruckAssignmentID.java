package com.freightbroker.broker_service.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class CargoTruckAssignmentID implements Serializable {
    private long id_cargo;
    private long id_truck;

    public CargoTruckAssignmentID(long id_cargo, long id_truck) {
        this.id_truck = id_truck;
        this.id_cargo = id_cargo;
    }
    public CargoTruckAssignmentID() {}
}
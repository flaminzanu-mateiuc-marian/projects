package com.freightbroker.broker_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cargos_trucks")
@NoArgsConstructor
@AllArgsConstructor
public class CargoTruckAssignmentDTO {
    @EmbeddedId
    public CargoTruckAssignmentID id;

    @Basic
    @Column(name = "profit")
    private double profit;
    @Basic
    @Column(name = "cost")
    private double cost;
    @Basic
    @Column(name="acceptat_de_client")
    private int accepted_by_client;
    @Basic
    @Column(name="acceptat_de_transportator")
    private int accepted_by_provider;

    public CargoTruckAssignmentDTO(CargoTruckAssignmentID id,double profit, double cost){
        this.id = id;
        this.profit = profit;
        this.cost = cost;
    }

}

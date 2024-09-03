package com.freightbroker.gateway_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trucks")
@NoArgsConstructor
@AllArgsConstructor
public class TruckDTO {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_truck")
    private long id_truck;

    @Basic
    @Column(name = "locatie_initiala")
    private String locatieInitiala;

    @Basic
    @Column(name = "volum_remorca")
    private long volum;

    @Basic
    @Column(name = "pret_km")
    private double pretKM;

    @Getter
    @Basic
    @Column(name = "cost_transport")
    private double costTransport;

    @Basic
    @Column(name = "id_transportator")
    private long idTransportator;

    @Basic
    @Column(name = "latitudine_locatie_initiala")
    private double latitudineLocatieInitiala;

    @Basic
    @Column(name = "longitudine_locatie_initiala")
    private double longitudineLocatieInitiala;

    @Basic
    @Column(name = "tonaj_maxim")
    private long tonajMaxim;

    @Basic
    @Column(name = "status")
    private String status;
}
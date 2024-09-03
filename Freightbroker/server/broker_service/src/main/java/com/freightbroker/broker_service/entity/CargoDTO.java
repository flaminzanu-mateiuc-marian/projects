package com.freightbroker.broker_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "cargos")
@NoArgsConstructor
@AllArgsConstructor
public class CargoDTO {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private long id_cargo;


    @Column(name="data_critica_livrare")
    private Date data_critica_livrare;

    @Basic
    @Column(name = "greutate")
    private long greutate;

    @Basic
    @Column(name = "volum")
    private long volum;


    @Basic
    @Column(name = "cerinte_suplimentare")
    private String cerinte_suplimentare;

    @Getter
    @Basic
    @Column(name = "adresa_ridicare")
    private String Adresa_ridicare;

    @Getter
    @Basic
    @Column(name = "adresa_livrare")
    private String Adresa_livrare;

    @Basic
    @Column(name = "comentarii")
    private String comentarii;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "id_comerciant")
    private long id_comerciant;

    @Basic
    @Column(name = "latitudine_pct_ridicare")
    private double latitudine_pct_ridicare;

    @Basic
    @Column(name = "longitudine_pct_ridicare")
    private double longitudine_pct_ridicare;

    @Basic
    @Column(name = "latitudine_pct_livrare")
    private double latitudine_pct_livrare;

    @Basic
    @Column(name = "longitudine_pct_livrare")
    private double longitudine_pct_livrare;


    @Basic
    @Column(name = "distanta_cursa")
    private double distanta_cursa;


    public String getComentarii() {
        return comentarii;
    }

}
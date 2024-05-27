package com.hospital.physician_service.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "doctori")

public class PhysicianDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private long id_doctor;
    @Basic
    @Column(name = "id_user")
    private int id_user;
    @Basic
    @Column(name = "nume")
    private String nume;
    @Basic
    @Column(name = "prenume")
    private String prenume;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "telefon")
    private String telefon;
    @Getter
    @Basic
    @Column(name = "specializare")
    private String specializare;

    public PhysicianDTO() {
    }

    public PhysicianDTO(long id_doctor, int id_user, String nume, String prenume, String email, String telefon, String specializare) {
        this.id_doctor = id_doctor;
        this.id_user = id_user;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
        this.specializare = specializare;
    }

}

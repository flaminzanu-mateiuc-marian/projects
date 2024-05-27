package com.hospital.patient_service.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pacienti")
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @Id
    @Basic
    @Column(name = "cnp")
    private String cnp;
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
    @Basic
    @Column(name = "data_nasterii")
    private LocalDate data_nasterii;
    @Basic
    @Column(name = "is_active")
    private Boolean is_active;

}
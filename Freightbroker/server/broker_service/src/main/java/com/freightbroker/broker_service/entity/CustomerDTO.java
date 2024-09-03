package com.freightbroker.broker_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private long id_customer;
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
    @Column(name = "parola")
    private String parola;

    public CustomerDTO(CustomerDTO other) {
        this.id_customer = other.id_customer;
        this.nume = other.nume;
        this.prenume = other.prenume;
        this.email = other.email;
        this.telefon = other.telefon;
        this.parola = other.parola;
    }
}
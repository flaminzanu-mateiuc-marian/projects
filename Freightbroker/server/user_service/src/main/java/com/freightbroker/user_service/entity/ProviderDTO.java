package com.freightbroker.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "providers")
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provider")
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
    @Column(name = "adresa")
    private String adresa;
    @Basic
    @Column(name = "parola")
    private String parola;

    public ProviderDTO(ProviderDTO other) {
        this.id_customer = other.id_customer;
        this.nume = other.nume;
        this.prenume = other.prenume;
        this.email = other.email;
        this.telefon = other.telefon;
        this.parola = other.parola;
        this.adresa = other.adresa;
    }

}
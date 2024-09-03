package com.freightbroker.payment_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
//@Table(name = "purchases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIdDTO implements Serializable {
    @Basic
    @Column(name = "payer_id")
    private String payerId;

    @Basic
    @Column(name = "payee_email")
    private String payeeEmail;

    @Basic
    @Column(name="total")
    private double total;
}
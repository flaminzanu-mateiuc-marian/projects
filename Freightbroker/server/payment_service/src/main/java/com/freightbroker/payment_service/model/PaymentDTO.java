package com.freightbroker.payment_service.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


import java.io.Serializable;

@Entity
@Table(name = "payments")
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {
    @EmbeddedId
    private PaymentIdDTO id;
}
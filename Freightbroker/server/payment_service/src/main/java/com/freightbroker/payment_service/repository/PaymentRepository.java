package com.freightbroker.payment_service.repository;

import com.freightbroker.payment_service.model.PaymentDTO;
import com.freightbroker.payment_service.model.PaymentIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDTO, PaymentIdDTO> {
}
package com.ecol.paymentService.repository;

import com.ecol.paymentService.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID > {

}

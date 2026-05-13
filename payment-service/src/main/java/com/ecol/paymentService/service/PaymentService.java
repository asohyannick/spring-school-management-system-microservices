package com.ecol.paymentService.service;

import com.ecol.paymentService.mapper.PaymentMapper;
import com.ecol.paymentService.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
 private final PaymentRepository paymentRepository;
 private final PaymentMapper paymentMapper;
}

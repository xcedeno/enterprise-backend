package com.xavier.enterprise.service;

import com.xavier.enterprise.event.PaymentCompletedEvent;
import com.xavier.enterprise.model.Payment;
import com.xavier.enterprise.producer.PaymentEventProducer;
import com.xavier.enterprise.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    public PaymentService(PaymentRepository paymentRepository, PaymentEventProducer paymentEventProducer) {
        this.paymentRepository = paymentRepository;
        this.paymentEventProducer = paymentEventProducer;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getPaymentsByApartment(String unit) {
        return paymentRepository.findByApartmentUnit(unit);
    }

    public Payment registerPayment(String residentName, String apartmentUnit, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }

        // 1. Guardar en Base de Datos Relacional
        Payment payment = new Payment(residentName, apartmentUnit, amount, "APPROVED");
        Payment saved = paymentRepository.save(payment);

        // 2. Disparar Evento Asíncrono en Kafka (Arquitectura Event-Driven)
        PaymentCompletedEvent event = new PaymentCompletedEvent(
            saved.getId(),
            saved.getResidentName(),
            saved.getApartmentUnit(),
            saved.getAmount(),
            saved.getStatus(),
            saved.getCreatedAt() != null ? saved.getCreatedAt() : LocalDateTime.now()
        );
        paymentEventProducer.publishPaymentEvent(event);

        return saved;
    }
}

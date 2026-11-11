package com.xavier.enterprise.controller;

import com.xavier.enterprise.model.Payment;
import com.xavier.enterprise.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*") // Permite que tu frontend de React se conecte sin problemas de CORS
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public record CreatePaymentRequest(
        String residentName,
        String apartmentUnit,
        BigDecimal amount
    ) {}

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/unit/{unit}")
    public List<Payment> getPaymentsByApartment(@PathVariable String unit) {
        return paymentService.getPaymentsByApartment(unit);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody CreatePaymentRequest request) {
        Payment created = paymentService.registerPayment(
            request.residentName(),
            request.apartmentUnit(),
            request.amount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}


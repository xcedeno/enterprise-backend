package com.xavier.enterprise;

import com.xavier.enterprise.controller.PaymentController;
import com.xavier.enterprise.model.Payment;
import com.xavier.enterprise.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    @DisplayName("GET /api/payments debe retornar la lista de pagos")
    void testGetAllPayments() {
        Payment p1 = new Payment("Carlos Mendoza", "4B", new BigDecimal("75.50"), "APPROVED");
        when(paymentService.getAllPayments()).thenReturn(List.of(p1));

        List<Payment> response = paymentController.getAllPayments();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Carlos Mendoza", response.get(0).getResidentName());
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    @DisplayName("POST /api/payments debe crear el pago con status CREATED (201)")
    void testCreatePayment() {
        Payment created = new Payment("Maria Delgado", "2A", new BigDecimal("120.00"), "APPROVED");
        when(paymentService.registerPayment("Maria Delgado", "2A", new BigDecimal("120.00"))).thenReturn(created);

        PaymentController.CreatePaymentRequest request = 
            new PaymentController.CreatePaymentRequest("Maria Delgado", "2A", new BigDecimal("120.00"));

        ResponseEntity<Payment> response = paymentController.createPayment(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Maria Delgado", response.getBody().getResidentName());
        verify(paymentService, times(1)).registerPayment("Maria Delgado", "2A", new BigDecimal("120.00"));
    }
}

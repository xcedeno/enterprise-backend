package com.xavier.enterprise;

import com.xavier.enterprise.model.Payment;
import com.xavier.enterprise.producer.PaymentEventProducer;
import com.xavier.enterprise.repository.PaymentRepository;
import com.xavier.enterprise.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("Debe registrar un pago exitoso y emitir el evento a Kafka")
    void testRegisterPaymentSuccess() {
        Payment mockSaved = new Payment("Xavier Cedeño", "PH-1", new BigDecimal("150.00"), "APPROVED");
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockSaved);

        Payment result = paymentService.registerPayment("Xavier Cedeño", "PH-1", new BigDecimal("150.00"));

        assertNotNull(result);
        assertEquals("Xavier Cedeño", result.getResidentName());
        assertEquals("PH-1", result.getApartmentUnit());
        assertEquals("APPROVED", result.getStatus());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(paymentEventProducer, times(1)).publishPaymentEvent(any());
    }

    @Test
    @DisplayName("Debe rechazar un pago con monto negativo o cero")
    void testRegisterPaymentNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.registerPayment("Inquilino Test", "1A", new BigDecimal("-10.00"));
        });

        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(paymentEventProducer);
    }
}


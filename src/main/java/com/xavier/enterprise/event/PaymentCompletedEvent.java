package com.xavier.enterprise.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentCompletedEvent(
    Long paymentId,
    String residentName,
    String apartmentUnit,
    BigDecimal amount,
    String status,
    LocalDateTime timestamp
) {}


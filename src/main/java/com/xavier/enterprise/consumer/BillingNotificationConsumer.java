package com.xavier.enterprise.consumer;

import com.xavier.enterprise.event.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BillingNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(BillingNotificationConsumer.class);

    @KafkaListener(topics = "pagos-completados", groupId = "facturacion-notifications-group")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("📥 [KAFKA CONSUMER] Evento recibido desde Kafka para el apartamento {}: Pago de ${}", 
            event.apartmentUnit(), event.amount());
        log.info("🧾 [FACTURACIÓN AUTOMÁTICA] Generando recibo digital para {}...", event.residentName());
        log.info("📲 [NOTIFICACIÓN] Enviando confirmación de pago al residente.");
    }
}


package com.xavier.enterprise.producer;

import com.xavier.enterprise.event.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventProducer.class);
    public static final String TOPIC_NAME = "pagos-completados";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentEvent(PaymentCompletedEvent event) {
        try {
            log.info("📢 [KAFKA PRODUCER] Publicando evento al topic '{}' con clave de partición '{}': {}", 
                TOPIC_NAME, event.apartmentUnit(), event);
            kafkaTemplate.send(TOPIC_NAME, event.apartmentUnit(), event);
        } catch (Exception e) {
            log.warn("⚠️ [KAFKA PRODUCER] No se pudo conectar al broker Kafka (¿está Kafka apagado?). Evento registrado localmente: {}", e.getMessage());
        }
    }
}


package com.fullcycle.codeflix.subscription.infrastructure.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fullcycle.codeflix.subscription.infrastructure.configuration.json.Json;
import com.fullcycle.codeflix.subscription.infrastructure.kafka.models.connect.MessageValue;
import com.fullcycle.codeflix.subscription.infrastructure.kafka.models.event.EventMessage;
import com.fullcycle.codeflix.subscription.infrastructure.mediator.EventProcessingMediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(EventListener.class);
    public static final TypeReference<MessageValue<EventMessage>> EVENT_MESSAGE = new TypeReference<>() {
    };

    private final EventProcessingMediator eventProcessingMediator;

    public EventListener(final EventProcessingMediator eventProcessingMediator) {
        this.eventProcessingMediator = Objects.requireNonNull(eventProcessingMediator);
    }

    @KafkaListener(
            concurrency = "${kafka.consumers.events.concurrency}",
            containerFactory = "kafkaListenerFactory",
            topics = "${kafka.consumers.events.topics}",
            groupId = "${kafka.consumers.events.group-id}",
            id = "${kafka.consumers.events.id}",
            properties = {
                    "auto.offset.reset=${kafka.consumers.events.auto-offset-reset}"
            }
    )
    public void onMessage(
            @Payload(required = false) final String payload,
            final ConsumerRecordMetadata metadata,
            final Acknowledgment ack
    ) {
        if (payload == null) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: EMPTY", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }

        LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: {}", metadata.topic(), metadata.partition(), metadata.offset(), payload);
        final var messagePayload = Json.readValue(payload, EVENT_MESSAGE).payload();
        if (!messagePayload.operation().isCreate()) {
            LOG.info("Message received from Kafka [topic:{}] [partition:{}] [offset:{}]: Discarded. Not create operation", metadata.topic(), metadata.partition(), metadata.offset());
            return;
        }

        this.eventProcessingMediator.execute(messagePayload.after().id());
        ack.acknowledge();
    }
}

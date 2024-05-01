package com.fullcycle.subscription.infrastructure.kafka;

import com.fullcycle.subscription.AbstractEmbeddedKafkaTest;
import com.fullcycle.subscription.domain.utils.IdUtils;
import com.fullcycle.subscription.infrastructure.json.Json;
import com.fullcycle.subscription.infrastructure.kafka.models.connect.MessageValue;
import com.fullcycle.subscription.infrastructure.kafka.models.connect.Operation;
import com.fullcycle.subscription.infrastructure.kafka.models.connect.ValuePayload;
import com.fullcycle.subscription.infrastructure.kafka.models.event.EventMsg;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class EventTableListenerTest extends AbstractEmbeddedKafkaTest {

    @SpyBean
    private EventTableListener eventTableListener;

    @Value("${kafka.consumers.events.topics}")
    private String eventsTopics;

    @Captor
    private ArgumentCaptor<ConsumerRecordMetadata> metadata;

    @Test
    public void testEventTopics() throws Exception {
        // given
        final var expectedMainTopic = "subscription.subscription.events";

        // when
        final var actualTopics = admin().listTopics().listings().get(10, TimeUnit.SECONDS).stream()
                .map(TopicListing::name)
                .collect(Collectors.toSet());

        // then
        Assertions.assertTrue(actualTopics.contains(expectedMainTopic));
    }

    @Test
    public void givenValidVideoWhenUpdateOperationProcessGoesOKThenShouldEndTheOperation() throws Exception {
        // given
        final var golangEv = new EventMsg(IdUtils.uniqueId());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(golangEv, golangEv, aSource(), Operation.UPDATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(eventTableListener).onMessage(any(), any());

        // when
        producer().send(new ProducerRecord<>(eventsTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
    }

    @Test
    public void givenCreateOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var golangEv = new EventMsg(IdUtils.uniqueId());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(golangEv, null, aSource(), Operation.CREATE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(eventTableListener).onMessage(any(), any());

        // when
        producer().send(new ProducerRecord<>(eventsTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
    }

    @Test
    public void givenDeleteOperationWhenProcessGoesOKShouldEndTheOperation() throws Exception {
        // given
        final var golangEv = new EventMsg(IdUtils.uniqueId());

        final var message =
                Json.writeValueAsString(new MessageValue<>(new ValuePayload<>(null, golangEv, aSource(), Operation.DELETE)));

        final var latch = new CountDownLatch(1);

        doAnswer(t -> {
            latch.countDown();
            return null;
        }).when(eventTableListener).onMessage(any(), any());

        // when
        producer().send(new ProducerRecord<>(eventsTopics, message)).get(10, TimeUnit.SECONDS);

        Assertions.assertTrue(latch.await(1, TimeUnit.MINUTES));

        // then
    }
}
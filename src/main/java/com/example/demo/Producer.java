package com.example.demo;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Producer {
  @Value("${app.kafka.topic.name}")
  private String topicName;

  private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

  CompletableFuture<SendResult<String, NotificationEvent>> write(String message) {
    log.info("tccl={}", Thread.currentThread().getContextClassLoader());

    var event = new NotificationEvent(UUID.randomUUID().toString(), message, LocalDateTime.now());
    return kafkaTemplate.send(topicName, event);
  }

  String printHeaders(SendResult<String, NotificationEvent> sendResult) {
    return StreamSupport.stream(sendResult.getProducerRecord().headers().spliterator(), false)
        .map(header -> header.key() + " :: " + new String(header.value()))
        .toList()
        .toString();
  }
}

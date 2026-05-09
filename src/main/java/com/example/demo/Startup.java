package com.example.demo;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Startup {
  private final Producer producer;
  private final MongoRepo repo;

  @EventListener
  public Mono<Void> onApplicationEvent(ContextRefreshedEvent ignored) {
    log.info("JOB tccl={}", Thread.currentThread().getContextClassLoader());

    return repo.findAll()
        .switchIfEmpty(
            repo.save(
                new NotificationEvent(
                    UUID.randomUUID().toString(), "Hello World!", LocalDateTime.now())))
        .flatMap(
            _ -> {
              log.info("flatmap tccl={}", Thread.currentThread().getContextClassLoader());

              return Mono.fromFuture(() -> producer.write("running withing a scheduled job"))
                  .map(producer::printHeaders);
            })
        .doOnNext(log::info)
        .then();
  }
}

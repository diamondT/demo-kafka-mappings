package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class Controller {
  private final Producer producer;

  @PostMapping("/write")
  public Mono<String> write() {
    return Mono.fromFuture(() -> producer.write("hi there bud")).map(producer::printHeaders);
  }
}

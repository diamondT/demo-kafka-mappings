package com.example.demo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
public class MongoConfig {
  @Primary
  @Bean
  public ReactiveMongoTemplate mongoTemplate(
      MongoClient mongoClient, @Value("${spring.mongodb.database}") String dbName) {
    return new ReactiveMongoTemplate(mongoClient, dbName);
  }

  @Primary
  @Bean
  public MongoClient mongoClient(@Value("${spring.mongodb.uri}") String uri) {
    return MongoClients.create(getMongoClientSettings(uri));
  }

  private MongoClientSettings getMongoClientSettings(String uri) {
    return MongoClientSettings.builder().applyConnectionString(new ConnectionString(uri)).build();
  }

  @Configuration
  @EnableReactiveMongoRepositories(
      basePackages = "com.example.demo",
      reactiveMongoTemplateRef = "mongoTemplate")
  public static class MongoDBConfig {}
}

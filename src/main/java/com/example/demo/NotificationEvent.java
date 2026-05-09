package com.example.demo;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notification-event")
public record NotificationEvent(@Id @Indexed String id, String message, LocalDateTime timestamp) {}

package com.ecol.authService.service;
import com.ecol.authService.config.kafkaConfig.KafkaTopics;
import com.ecol.authService.dto.PasswordResetRequestedEvent;
import com.ecol.authService.dto.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthEventProducer {

private final KafkaTemplate<String, Object> kafkaTemplate;

public void publishUserRegistered(UserRegisteredEvent event) {
	CompletableFuture<SendResult<String, Object>> future =
			kafkaTemplate.send(KafkaTopics.USER_REGISTERED, event.userId(), event); // ✅ event.userId()
	
	future.whenComplete((result, ex) -> {
		if (ex != null) {
			log.error("❌ Failed to publish UserRegisteredEvent for userId={} | error={}",
					event.userId(), ex.getMessage()); // ✅ event.userId()
		} else {
			log.info("✅ Published UserRegisteredEvent | userId={} | topic={} | partition={} | offset={}",
					event.userId(),                               // ✅
					result.getRecordMetadata().topic(),
					result.getRecordMetadata().partition(),
					result.getRecordMetadata().offset());
		}
	});
}

public void publishPasswordResetRequested(PasswordResetRequestedEvent event) {
	CompletableFuture<SendResult<String, Object>> future =
			kafkaTemplate.send(KafkaTopics.PASSWORD_RESET_REQUESTED, event.userId(), event); // ✅ event.userId()
	
	future.whenComplete((result, ex) -> {
		if (ex != null) {
			log.error("❌ Failed to publish PasswordResetRequestedEvent for userId={} | error={}",
					event.userId(), ex.getMessage()); // ✅
		} else {
			log.info("✅ Published PasswordResetRequestedEvent | userId={} | topic={}",
					event.userId(), result.getRecordMetadata().topic()); // ✅
		}
	});
}

public void publishAccountLocked(String userId, String email, String reason) {
	var payload = new java.util.HashMap<String, Object>();
	payload.put("userId", userId);
	payload.put("email", email);
	payload.put("reason", reason);
	payload.put("lockedAt", java.time.LocalDateTime.now().toString());
	
	kafkaTemplate.send(KafkaTopics.ACCOUNT_LOCKED, userId, payload);
	log.warn("🔒 Published AccountLockedEvent | userId={} | reason={}", userId, reason);
}
}
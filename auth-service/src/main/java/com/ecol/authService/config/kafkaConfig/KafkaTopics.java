package com.ecol.authService.config.kafkaConfig;

public final class KafkaTopics {
	// Utility class — no instantiation
	  private KafkaTopics() {}
	// authService PRODUCES these
    public static  final  String USER_REGISTERED = "user.registered";
	public static final String PASSWORD_RESET_REQUESTED = "user.password-reset-requested";
	public static final String ACCOUNT_LOCKED           = "user.account-locked";
	public static final String EMAIL_VERIFIED           = "user.email-verified";
	public static final String TOKEN_REVOKED            = "user.token-revoked";
	// authService CONSUMES these (from other services)
	public  static  final String PAYMENT_CONFIRMED       = "user.payment-confirmed";
	public static  final String ENROLLMENT_CREATED =  "user.enrollment-created";
	
}

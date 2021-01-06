package br.com.andrade.challengeauthorizer.domain;

public enum MessageBusinessRulesEnum {
	
	ACCCOUNT_ALREADY_INITIALIZED("account-already-initialized"),
	ACCCOUNT_NOT_INITIALIZED("account-not-initialized"), 
	CARD_NOT_ACTIVE("card-not-active"),
	INSUFFICIENT_LIMIT("insufficient-limit"), 
	HIGH_FREQUENCY_SMALL_INTERVAL("high-frequency-small-interval"),
	DOUBLED_TRANSACTION("doubled-transaction");

	private String message;

	private MessageBusinessRulesEnum(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}

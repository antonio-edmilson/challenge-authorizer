package br.com.andrade.challengeauthorizer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CreditCard {
	
	@JsonProperty("available-limit")
	private Integer limitAvailable;
	
	@JsonProperty("active-card")
	private Boolean activedCard;
	
	public Integer getLimitAvailable() {
		return limitAvailable;
	}
	
	public Boolean getActivedCard() {
		return activedCard;
	}
}

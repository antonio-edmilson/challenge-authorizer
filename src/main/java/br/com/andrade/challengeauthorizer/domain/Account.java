package br.com.andrade.challengeauthorizer.domain;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@AllArgsConstructor
public class Account {
	

	@JsonIgnore
	private Integer id;
	
	@JsonProperty("account")
	private CreditCard creditCard;

	@JsonProperty("violations")
	@NonFinal
	private Set<String> violations = new HashSet<>();	

}

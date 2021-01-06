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
public class TransactionAuthorization {

	@JsonIgnore
	private Integer id;
	
	@JsonProperty("transaction")
	private Transaction transaction;

	@JsonProperty("violations")
	@NonFinal
	private Set<String> violations = new HashSet<>();

}

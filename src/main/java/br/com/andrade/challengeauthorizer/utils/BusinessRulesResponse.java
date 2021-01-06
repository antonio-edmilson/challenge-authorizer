package br.com.andrade.challengeauthorizer.utils;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class BusinessRulesResponse {

	private Set<String> violations = new HashSet<>();

	public Boolean isValidBusiness() {
		return violations.isEmpty();
	}
}

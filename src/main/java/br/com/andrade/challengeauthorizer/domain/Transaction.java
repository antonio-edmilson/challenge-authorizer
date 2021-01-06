package br.com.andrade.challengeauthorizer.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Transaction{
	
	private String merchant;
	private Integer amount;
	private LocalDateTime time;
	
	@Override
	public boolean equals(Object obj) {
		Transaction transaction = (Transaction) obj;
		if (transaction == null) {
			return Boolean.FALSE;
		}
		return merchant.equals(transaction.merchant) && amount.equals(transaction.amount);

	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}

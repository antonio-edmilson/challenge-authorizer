package br.com.andrade.challengeauthorizer.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import br.com.andrade.challengeauthorizer.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class responsible for managing API cache requests
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheResponse {

	private LocalDateTime lastRequestHourValid;
	private Integer numberRequisition = 1;
	private Set<Transaction> transactions = new HashSet<>();
	
	public void addNumberRequisition() {
		if (lastRequestHourValid != null) {
			Duration duration = Duration.between(lastRequestHourValid, LocalDateTime.now());
			if (duration.toMinutes() == 0) {
				numberRequisition++;
			} else {
				numberRequisition = 1;
				transactions.clear();
			}
		}

	}
	
	public void addCacheRequisition(Transaction transaction) {
		addNumberRequisition();
		if(numberRequisition.equals(1)) {
			addLastRequestHourValid();
		}
		Duration duration = Duration.between(lastRequestHourValid, LocalDateTime.now());
		if (duration.toMinutes() != 0) {
			resetCache();
		}
	}

	private void resetCache() {
		numberRequisition = 1;
		transactions.clear();
		lastRequestHourValid = null;
	}
	
	public void addLastRequestHourValid() {
		lastRequestHourValid = LocalDateTime.now();
	}
	
	public void addTransactions(Transaction transaction) {
		transactions.add(transaction);
	}
}

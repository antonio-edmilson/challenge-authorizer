package br.com.andrade.challengeauthorizer.respository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import br.com.andrade.challengeauthorizer.domain.Transaction;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import reactor.core.publisher.Mono;

@Repository
public class TransactionAuthorizationRepositoryImpl implements TransactionAuthorizationRepository {

	// Mechanism used to work with data in memory
	private Set<TransactionAuthorization> dataBaseTransactionInMemory = new HashSet<>();

	@Override
	public Mono<Boolean> save(TransactionAuthorization transaction) {
		dataBaseTransactionInMemory.add(setIdAccount(transaction.getTransaction()));
		return Mono.just(Boolean.TRUE);
	}

	@Override
	public Set<TransactionAuthorization> getListTransaction() {
		return dataBaseTransactionInMemory;
	}

	@Override
	public Integer getTotalTransaction() {
		return dataBaseTransactionInMemory.stream().mapToInt(x -> x.getTransaction().getAmount()).reduce(0,
				Integer::sum);
	}

	private TransactionAuthorization setIdAccount(Transaction transaction) {
		Optional<Integer> idOption = dataBaseTransactionInMemory.stream().map(TransactionAuthorization::getId)
				.max(Integer::compare);
		Integer id = idOption.isPresent() ? idOption.get() : 1;
		return new TransactionAuthorization(id,
				new Transaction(transaction.getMerchant(), transaction.getAmount(), transaction.getTime()),
				new HashSet<>());
	}

}

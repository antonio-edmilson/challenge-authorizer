package br.com.andrade.challengeauthorizer.respository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	// Mechanism used to work with data in memory
	private Set<Account> dataBaseAccountInMemory = new HashSet<>();

	@Override
	public Mono<Account> save(Account account) {
		dataBaseAccountInMemory.add(setIdAccount(account));
		return Mono.just(new Account(account.getId(),
				new CreditCard(account.getCreditCard().getLimitAvailable(), account.getCreditCard().getActivedCard()),
				new HashSet<>()));
	}

	@Override
	public Flux<Account> findAll() {
		return Flux.fromStream(dataBaseAccountInMemory.stream());
	}

	private Account setIdAccount(Account account) {
		Optional<Integer> idOption = dataBaseAccountInMemory.stream().map(Account::getId).max(Integer::compare);
		Integer id = idOption.isPresent() ? idOption.get() : 1;
		return new Account(id,
				new CreditCard(account.getCreditCard().getLimitAvailable(), account.getCreditCard().getActivedCard()),
				new HashSet<>());
	}

	public Set<Account> getListAccount() {
		return dataBaseAccountInMemory;
	}

}

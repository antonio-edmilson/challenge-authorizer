package br.com.andrade.challengeauthorizer.respository;

import br.com.andrade.challengeauthorizer.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository {
	Mono<Account> save(Account account);

	Flux<Account> findAll();
}

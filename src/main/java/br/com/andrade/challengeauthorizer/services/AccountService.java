package br.com.andrade.challengeauthorizer.services;

import br.com.andrade.challengeauthorizer.domain.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {

	Mono<Account> save(Account account);

	Flux<Account> findAll();
}

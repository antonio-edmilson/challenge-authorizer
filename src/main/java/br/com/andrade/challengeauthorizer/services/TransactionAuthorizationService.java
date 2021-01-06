package br.com.andrade.challengeauthorizer.services;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import reactor.core.publisher.Mono;

public interface TransactionAuthorizationService {

	Mono<Account> save(TransactionAuthorization transaction);
}

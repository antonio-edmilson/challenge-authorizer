package br.com.andrade.challengeauthorizer.respository;

import java.util.Set;

import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import reactor.core.publisher.Mono;

public interface TransactionAuthorizationRepository {
	Mono<Boolean> save(TransactionAuthorization transaction);

	Set<TransactionAuthorization> getListTransaction();

	Integer getTotalTransaction();
}

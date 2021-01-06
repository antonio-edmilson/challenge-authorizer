package br.com.andrade.challengeauthorizer.controller;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import br.com.andrade.challengeauthorizer.services.TransactionAuthorizationService;
import reactor.core.publisher.Mono;

@Component
public class TransactionAuthorizationHandler {

	@Autowired
	private TransactionAuthorizationService transactionService;
	
	public Mono<ServerResponse> save(ServerRequest request) {
		final Mono<TransactionAuthorization> transaction = request.bodyToMono(TransactionAuthorization.class);

		return ok().contentType(MediaType.APPLICATION_JSON)
				.body(fromPublisher(transaction.flatMap(transactionService::save), Account.class));
	}
}

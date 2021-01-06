package br.com.andrade.challengeauthorizer.controller;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.services.AccountService;
import reactor.core.publisher.Mono;

@Component
public class AccountHandler {

	@Autowired
	private AccountService accountService;
	
	public Mono<ServerResponse> save(ServerRequest request) {
		final Mono<Account> account = request.bodyToMono(Account.class);

		return ok().contentType(MediaType.APPLICATION_JSON)
				.body(fromPublisher(account.flatMap(accountService::save), Account.class));
	}
}

package br.com.andrader.challengeauthorizer;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.andrade.challengeauthorizer.controller.AccountHandler;
import br.com.andrade.challengeauthorizer.controller.AccountRouter;
import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.CreditCard;
import br.com.andrade.challengeauthorizer.domain.Transaction;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import br.com.andrade.challengeauthorizer.respository.AccountRepository;
import br.com.andrade.challengeauthorizer.respository.AccountRepositoryImpl;
import br.com.andrade.challengeauthorizer.services.AccountService;
import br.com.andrade.challengeauthorizer.services.AccountServiceImpl;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AccountRouter.class, AccountHandler.class, AccountService.class,
		AccountServiceImpl.class, AccountRepository.class, AccountRepositoryImpl.class })
@WebFluxTest(AccountTestIntegration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountTestIntegration {

	@Autowired
	private WebTestClient client;

	@Test()
	@Order(1)
	public void testCreateAccount() {
		Account account = new Account(1, new CreditCard(500, true), new HashSet<>());
		client.post()
			.uri("/account")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(account), Account.class)
			.exchange()
			.expectStatus().isOk();
	}

	@Test
	@Order(2)
	public void testBusinessRuleViolationAccountAlreadyInitialized() {
		Account account = new Account(1, new CreditCard(500, true), new HashSet<>());
		client.post()
			.uri("/account")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(account), Account.class)
			.exchange().expectStatus()
			.isOk()
			.expectBody(Account.class).value(accountResponse -> {
				Assertions.assertTrue(!accountResponse.getViolations().isEmpty());
			});
	}

	private TransactionAuthorization generateTransaction() {
		return new TransactionAuthorization(1, new Transaction("Habbib's", 1000, LocalDateTime.now()), new HashSet<>());
	}
	

}

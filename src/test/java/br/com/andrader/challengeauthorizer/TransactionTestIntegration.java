package br.com.andrader.challengeauthorizer;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.andrade.challengeauthorizer.controller.TransactionAuthorizationHandler;
import br.com.andrade.challengeauthorizer.controller.TransactionAuthorizationRouter;
import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.CreditCard;
import br.com.andrade.challengeauthorizer.domain.MessageBusinessRulesEnum;
import br.com.andrade.challengeauthorizer.domain.Transaction;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import br.com.andrade.challengeauthorizer.respository.AccountRepositoryImpl;
import br.com.andrade.challengeauthorizer.respository.TransactionAuthorizationRepository;
import br.com.andrade.challengeauthorizer.respository.TransactionAuthorizationRepositoryImpl;
import br.com.andrade.challengeauthorizer.services.TransactionAuthorizationService;
import br.com.andrade.challengeauthorizer.services.TransactionAuthorizationServiceImpl;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TransactionAuthorizationRouter.class, TransactionAuthorizationHandler.class, TransactionAuthorizationService.class,
		TransactionAuthorizationServiceImpl.class, TransactionAuthorizationRepository.class,
		TransactionAuthorizationRepositoryImpl.class, AccountRepositoryImpl.class })
@WebFluxTest(TransactionTestIntegration.class)
@AutoConfigureWebTestClient(timeout = "36000")
public class TransactionTestIntegration {

	@Autowired
	private WebTestClient client;
	
	@MockBean
	private AccountRepositoryImpl accountRepositoryImpl;

	@Test()
	public void testCreateTransaction() {
		when(accountRepositoryImpl.getListAccount()).thenReturn(getListAccounts());
		
		TransactionAuthorization transaction = new TransactionAuthorization(1, new Transaction("Burger King", 1000, LocalDateTime.now()), new HashSet<>());
		client.post()
			.uri("/transaction")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(transaction), Account.class)
			.exchange()
			.expectStatus().isOk()
			.expectBody(Account.class).value(accountResponse -> {
				Assertions.assertTrue(accountResponse.getViolations().isEmpty());
			});
	}

	@Test
	public void testBusinessRuleThreeTransactionsIntervalTwoMinutes(){
		
		when(accountRepositoryImpl.getListAccount()).thenReturn(getListAccounts());
		
		IntStream.range(1,4).forEach(i -> {
			
				TransactionAuthorization transaction = new TransactionAuthorization(i, new Transaction("Burger King", i, LocalDateTime.now()), new HashSet<>());
				client.post()
					.uri("/transaction")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).body(Mono.just(transaction), Account.class).exchange()
					.expectStatus().isOk()
					.expectBody(Account.class)
					.value(accountResponse -> {
							if (i > 3) {
								Assertions.assertTrue(accountResponse.getViolations()
										.contains(MessageBusinessRulesEnum.HIGH_FREQUENCY_SMALL_INTERVAL.toString()));
							} else {
								Assertions.assertTrue(accountResponse.getViolations().isEmpty());
							}
						});

			
	    });
	}
	
	@Test
	public void testBusinessRuleSimilarTransactionIntervalTowMinutes() {
		
		when(accountRepositoryImpl.getListAccount()).thenReturn(getListAccounts());
		
		IntStream.range(1,2).forEach(i -> {
			
				TransactionAuthorization transaction = new TransactionAuthorization(1, new Transaction("Burger King", 10, LocalDateTime.now()), new HashSet<>());
				client.post()
					.uri("/transaction")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).body(Mono.just(transaction), Account.class).exchange()
					.expectStatus().isOk()
					.expectBody(Account.class)
					.value(accountResponse -> {
							if (i == 2) {
								Assertions.assertTrue(accountResponse.getViolations()
										.contains(MessageBusinessRulesEnum.DOUBLED_TRANSACTION.toString()));
							} else {
								Assertions.assertTrue(accountResponse.getViolations().isEmpty());
							}
						});

			
	    });
	}

	private Set<Account> getListAccounts() {
		Set<Account> accounts = new HashSet<>();
		accounts.add(new Account(1, new CreditCard(1000, true), new HashSet<>()));
		return accounts;
	}

}

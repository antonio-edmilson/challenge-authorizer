package br.com.andrader.challengeauthorizer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.CreditCard;
import br.com.andrade.challengeauthorizer.respository.AccountRepositoryImpl;
import br.com.andrade.challengeauthorizer.services.AccountServiceImpl;
import reactor.core.publisher.Mono;

@WebFluxTest(AccountTest.class)
public class AccountTest {

	@Autowired
	private AccountServiceImpl accountServiceImpl;

	@MockBean
	private AccountRepositoryImpl accountRespository;

	@Test
	public void testBusinessRuleViolationAccountAlreadyInitialized() {
		when(accountRespository.getListAccount())
				.thenReturn(new HashSet<>(Arrays.asList(new Account(1, new CreditCard(500, Boolean.TRUE), null))));

		assertTrue(!accountServiceImpl.save(generateAccount()).block().getViolations().isEmpty());

	}

	@Test
	public void testCreateAccountSucess() {
		when(accountRespository.save(any())).thenReturn(Mono.just(generateAccount()));
		assertTrue(accountServiceImpl.save(generateAccount()).block().getViolations().isEmpty());
	}

	private Account generateAccount() {
		return new Account(null, new CreditCard(1000, Boolean.TRUE), new HashSet<>());
	}

}

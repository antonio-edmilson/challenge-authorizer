package br.com.andrader.challengeauthorizer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.CreditCard;
import br.com.andrade.challengeauthorizer.domain.MessageBusinessRulesEnum;
import br.com.andrade.challengeauthorizer.domain.Transaction;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import br.com.andrade.challengeauthorizer.respository.AccountRepositoryImpl;
import br.com.andrade.challengeauthorizer.respository.TransactionAuthorizationRepositoryImpl;
import br.com.andrade.challengeauthorizer.services.TransactionAuthorizationServiceImpl;
import br.com.andrade.challengeauthorizer.utils.CacheResponse;
import reactor.core.publisher.Mono;

@WebFluxTest(TransactionTest.class)
public class TransactionTest {

	@Autowired
	private TransactionAuthorizationServiceImpl transactionServiceImpl;
	
	@MockBean
	private TransactionAuthorizationRepositoryImpl transactionRepository;
	
	@MockBean
	private AccountRepositoryImpl accountRespositoryImpl;
	

	@Test
	public void testBusinessRuleAccountAlreadyNotInitialized() {
		when(accountRespositoryImpl.getListAccount()).thenReturn(new HashSet<>());
		assertTrue(transactionServiceImpl.isValidBusinessRules(generateTransaction())
				.getViolations().contains(MessageBusinessRulesEnum.ACCCOUNT_NOT_INITIALIZED.getMessage()));

	}
	
	@Test
	public void testBusinessRuleCardNotActive() {
		when(accountRespositoryImpl.getListAccount())
			.thenReturn(new HashSet<>(Arrays.asList(new Account(1, new CreditCard(1000, Boolean.FALSE), null))));
		assertTrue(transactionServiceImpl.isValidBusinessRules(generateTransaction())
				.getViolations().contains(MessageBusinessRulesEnum.CARD_NOT_ACTIVE.getMessage()));

	}
	
	
	@Test
	public void testBusinessRuleInsufficientLimit() {
		when(accountRespositoryImpl.getListAccount())
		.thenReturn(new HashSet<>(Arrays.asList(new Account(1, new CreditCard(500, Boolean.FALSE), null))));
		assertTrue(transactionServiceImpl.isValidBusinessRules(generateTransaction()).getViolations()
				.contains(MessageBusinessRulesEnum.INSUFFICIENT_LIMIT.getMessage()));

	}

	@Test
	public void testCreateTransactionSucess() {
		when(accountRespositoryImpl.getListAccount()).thenReturn(new HashSet<>(Arrays.asList(generateAccount())));
		when(transactionRepository.save(any())).thenReturn(Mono.just(Boolean.TRUE));
		assertTrue(transactionServiceImpl.save(generateTransaction()).block().getViolations().isEmpty());
	}

	private TransactionAuthorization generateTransaction() {
		return new TransactionAuthorization(1, new Transaction("Habbib's", 1000, LocalDateTime.now()), new HashSet<>());
	}
	
	private Account generateAccount() {
		return new Account(null, new CreditCard(1000, Boolean.TRUE), new HashSet<>());
	}

}

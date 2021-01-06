package br.com.andrade.challengeauthorizer.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.CreditCard;
import br.com.andrade.challengeauthorizer.domain.MessageBusinessRulesEnum;
import br.com.andrade.challengeauthorizer.domain.Transaction;
import br.com.andrade.challengeauthorizer.domain.TransactionAuthorization;
import br.com.andrade.challengeauthorizer.respository.AccountRepositoryImpl;
import br.com.andrade.challengeauthorizer.respository.TransactionAuthorizationRepositoryImpl;
import br.com.andrade.challengeauthorizer.utils.BusinessRulesResponse;
import br.com.andrade.challengeauthorizer.utils.CacheResponse;
import reactor.core.publisher.Mono;

@Service
public class TransactionAuthorizationServiceImpl implements TransactionAuthorizationService {

	@Autowired
	private TransactionAuthorizationRepositoryImpl transactionRespository;

	@Autowired
	private AccountRepositoryImpl accountRespositoryImpl;

	private final CacheResponse cacheResponse = new CacheResponse();

	@Override
	public Mono<Account> save(final TransactionAuthorization transaction) {
		cacheResponse.addCacheRequisition(transaction.getTransaction());
		final BusinessRulesResponse businessRulesResponse = isValidBusinessRules(transaction);
		final Account account = getAccount();

		if (businessRulesResponse.isValidBusiness()) {
			transactionRespository.save(transaction);
			return getResponseSave(transaction, account, businessRulesResponse);
		} else {
			return getResponseSave(transaction, account, businessRulesResponse);
		}
	}
	
	public BusinessRulesResponse isValidBusinessRules(final TransactionAuthorization transaction) {
		final BusinessRulesResponse businessRulesResponse = new BusinessRulesResponse();
		final Set<Account> accounts = accountRespositoryImpl.getListAccount();

		if (isAccountAlreadyNotInitialized(accounts)) {
			businessRulesResponse.getViolations().add(MessageBusinessRulesEnum.ACCCOUNT_NOT_INITIALIZED.getMessage());
		}
		if (isThreeTransactionsIntervalTwoMinutes()) {
			businessRulesResponse.getViolations()
					.add(MessageBusinessRulesEnum.HIGH_FREQUENCY_SMALL_INTERVAL.getMessage());
		}
		if (isSimilarTransactionIntervalTowMinutes(transaction.getTransaction())) {
			businessRulesResponse.getViolations().add(MessageBusinessRulesEnum.DOUBLED_TRANSACTION.getMessage());
		}

		if (!accounts.isEmpty()) {
			if (isCardNotActive(accounts)) {
				businessRulesResponse.getViolations().add(MessageBusinessRulesEnum.CARD_NOT_ACTIVE.getMessage());
			}
			if (isInsufficientLimit(transaction.getTransaction().getAmount(), accounts)) {
				businessRulesResponse.getViolations().add(MessageBusinessRulesEnum.INSUFFICIENT_LIMIT.getMessage());
			}
		}

		return businessRulesResponse;
	}

	private Boolean isAccountAlreadyNotInitialized(Set<Account> accounts) {
		return accounts.isEmpty();
	}

	private Boolean isCardNotActive(final Set<Account> accounts) {
		return accounts.stream().anyMatch(account -> !account.getCreditCard().getActivedCard());
	}

	private Boolean isInsufficientLimit(final Integer transactionAmount, final Set<Account> accounts) {
		Account account = accounts.stream().findFirst().get();
		return transactionAmount.compareTo(getNewLimitAvailable(account)) > 0;
	}

	private Boolean isSimilarTransactionIntervalTowMinutes(final Transaction transaction) {
		if (cacheResponse.getLastRequestHourValid() != null) {
			Duration duration = Duration.between(cacheResponse.getLastRequestHourValid(), LocalDateTime.now());
			return duration.toMinutes() == 0
					&& cacheResponse.getTransactions().stream().anyMatch(p -> p.equals(transaction));
		}
		return Boolean.FALSE;
	}

	private Boolean isThreeTransactionsIntervalTwoMinutes() {
		if (cacheResponse.getLastRequestHourValid() != null) {
			Duration duration = Duration.between(cacheResponse.getLastRequestHourValid(), LocalDateTime.now());
			return duration.toMinutes() == 0 && cacheResponse.getNumberRequisition().compareTo(3) > 0;
		}
		return Boolean.FALSE;
	}
	
	private Integer getNewLimitAvailable(final Account account) {
		return account.getCreditCard().getLimitAvailable() - transactionRespository.getTotalTransaction();
	}
	
	private Mono<Account> getResponseSave(TransactionAuthorization transaction, final Account account,
			final BusinessRulesResponse businessRulesResponse) {
		cacheResponse.addTransactions(transaction.getTransaction());
		
		if(businessRulesResponse.getViolations().contains(MessageBusinessRulesEnum.ACCCOUNT_NOT_INITIALIZED.getMessage())) {
			return Mono.just(new Account(account.getId(), null, businessRulesResponse.getViolations()));
		}else {
			CreditCard creditCard = new CreditCard(getNewLimitAvailable(account), account.getCreditCard().getActivedCard());
			return Mono.just(new Account(account.getId(), creditCard, businessRulesResponse.getViolations()));
		}
	}

	private Account getAccount() {
		return accountRespositoryImpl.getListAccount().stream()
				.findFirst().orElse(new Account(null, null, new HashSet<>()));
	}
}

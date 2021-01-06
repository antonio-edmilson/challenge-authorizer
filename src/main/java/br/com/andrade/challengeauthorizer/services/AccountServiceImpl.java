package br.com.andrade.challengeauthorizer.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.andrade.challengeauthorizer.domain.Account;
import br.com.andrade.challengeauthorizer.domain.MessageBusinessRulesEnum;
import br.com.andrade.challengeauthorizer.respository.AccountRepositoryImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepositoryImpl accountRespository;

	@Override
	public Mono<Account> save(final Account account) {
		if (verifyAccountAlreadyNotInitialized()) {
			return accountRespository.save(account);
		} else {
			Set<String> messageErrors = new HashSet<>();
			messageErrors.add(MessageBusinessRulesEnum.ACCCOUNT_ALREADY_INITIALIZED.getMessage());
			return Mono.just(new Account(null, account.getCreditCard(), messageErrors));
		}
	}

	@Override
	public Flux<Account> findAll() {
		return accountRespository.findAll();
	}

	private Boolean verifyAccountAlreadyNotInitialized() {
		return accountRespository.getListAccount().isEmpty();
	}

}

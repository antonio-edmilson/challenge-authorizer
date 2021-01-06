package br.com.andrade.challengeauthorizer.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransactionAuthorizationRouter {
	
	@Bean
	public RouterFunction<ServerResponse> routeTransaction(TransactionAuthorizationHandler handler) {
		return RouterFunctions
				.route(POST("/transaction")
						.and(accept(MediaType.APPLICATION_JSON)), handler::save);
	}

}

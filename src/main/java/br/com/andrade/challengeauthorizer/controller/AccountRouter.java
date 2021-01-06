package br.com.andrade.challengeauthorizer.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AccountRouter {
	
	@Bean
	public RouterFunction<ServerResponse> routeAccount(AccountHandler handler) {
		return RouterFunctions
				.route(POST("/account")
						.and(accept(MediaType.APPLICATION_JSON)), handler::save);
	}

}
package br.com.andrade.challengeauthorizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "br.com.andrade.challengeauthorizer", "org.springdoc.webflux.ui", "org.springdoc.webmvc.ui" })
public class ChallengeAuthorizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeAuthorizerApplication.class, args);
	}
}

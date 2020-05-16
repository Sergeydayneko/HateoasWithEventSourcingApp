package ru.dayneko.config.jackson;

import com.fasterxml.jackson.databind.Module;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures custom serialization and deserialization of {@link Money} instances
 */
@Configuration
class JacksonCustomizations {

	@Bean
	public Module moneyModule() {
		return new MoneyModule();
	}

	@Bean
	public Module restbucksModule() {
		return new ShopModule();
	}
}

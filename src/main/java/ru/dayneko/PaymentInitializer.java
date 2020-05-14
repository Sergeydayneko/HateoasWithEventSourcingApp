package ru.dayneko;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.dayneko.payment.model.CreditCard;
import ru.dayneko.payment.model.CreditCardNumber;
import ru.dayneko.payment.repository.CreditCardRepository;

import java.time.Month;
import java.time.Year;

/**
 * Initializing component to create a default {@link CreditCard} in the system.
 */
@Service
@Slf4j
@RequiredArgsConstructor
class PaymentInitializer {

	private final CreditCardRepository repository;

	@EventListener
	public void init(ApplicationReadyEvent event) {

		if (repository.count() > 0) {
			return;
		}

		CreditCardNumber number = new CreditCardNumber("1234123412341234");
		CreditCard creditCard = new CreditCard(number, "Oliver Gierke", Month.DECEMBER, Year.of(2099));

		creditCard = repository.save(creditCard);

		log.info("Credit card {} created!", creditCard);
	}
}

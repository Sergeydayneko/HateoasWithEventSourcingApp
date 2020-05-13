package ru.dayneko.payment.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dayneko.payment.model.CreditCard;
import ru.dayneko.payment.model.CreditCardNumber;

import java.util.Optional;

/**
 * Repository to access {@link CreditCard} instances.
 */
public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {

	Optional<CreditCard> findByNumber(CreditCardNumber number);
}

package ru.dayneko.payment.model;

import lombok.*;
import ru.dayneko.core.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * Abstraction of a credit card.
 */
@Entity
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CreditCard extends AbstractEntity {

	@Getter
	private final CreditCardNumber number;

	@Getter
	private final String cardHolderName;

	private Month expiryMonth;
	private Year expiryYear;

	public boolean isValid() {
		return isValid(LocalDate.now());
	}

	public boolean isValid(LocalDate date) {
		return date != null && getExpirationDate().isAfter(date);
	}

	public LocalDate getExpirationDate() {
		return LocalDate.of(expiryYear.getValue(), expiryMonth, 1);
	}

	protected void setExpirationDate(LocalDate date) {

		this.expiryYear = Year.of(date.getYear());
		this.expiryMonth = date.getMonth();
	}
}

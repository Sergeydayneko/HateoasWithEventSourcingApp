package ru.dayneko.payment.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

/**
 * Value object to represent a {@link CreditCardNumber}.
 */
@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class CreditCardNumber {

	public static final String REGEX = "[0-9]{16}";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

	@Column(unique = true)
	private final String number;

	public CreditCardNumber(String number) {

		if (!isValid(number)) {
			throw new IllegalArgumentException(String.format("Invalid credit card number %s!", number));
		}

		this.number = number;
	}

	@Override
	public String toString() {
		return number;
	}

	/**
	 * Returns whether the given {@link String} is a valid {@link CreditCardNumber}
	 * Also can validated by JPA @CreditCardNumber
	 */
	public static boolean isValid(String number) {
		return number != null && PATTERN.matcher(number).matches();
	}
}

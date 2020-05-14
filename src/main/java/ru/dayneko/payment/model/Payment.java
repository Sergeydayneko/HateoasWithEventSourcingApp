package ru.dayneko.payment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import ru.dayneko.core.AbstractEntity;
import ru.dayneko.order.model.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Base class for payment implementations.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(force = true)
public abstract class Payment extends AbstractEntity {

	@JoinColumn(name = "rborder")
	@OneToOne(cascade = CascadeType.MERGE)
	private final Order order;

	private final LocalDateTime paymentDate;

	/**
	 * Creates a new {@link Payment} referring to the given {@link Order}.
	 */
	public Payment(@NotNull Order order) {

		this.order = order;
		this.paymentDate = LocalDateTime.now();
	}

	/**
	 * Returns a receipt for the {@link Payment}.
	 */
	public Receipt getReceipt() {
		return new Receipt(paymentDate, order);
	}

	/**
	 * A receipt for an {@link Order} and a payment date.
	 */
	@Value
	public static class Receipt {

		private final LocalDateTime date;
		private final Order order;
	}
}

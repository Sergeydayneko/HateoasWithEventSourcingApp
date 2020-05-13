package ru.dayneko.order.model;

import lombok.Getter;
import lombok.ToString;
import org.javamoney.moneta.Money;
import ru.dayneko.core.AbstractAggregateRoot;
import ru.dayneko.payment.OrderPaid;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An order.
 */
@Entity
@Getter
@ToString(exclude = "lineItems")
@Table(name = "RBOrder")
public class Order extends AbstractAggregateRoot {

	private final Location location;
	private final LocalDateTime orderedDate;
	private Status status;

	@OrderColumn
	@Column(unique = true) //
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //
	private final List<LineItem> lineItems = new ArrayList<>();

	public Order(Collection<LineItem> lineItems, Location location) {

		this.location = location == null ? Location.TAKE_AWAY : location;
		this.status = Status.PAYMENT_EXPECTED;
		this.lineItems.addAll(lineItems);
		this.orderedDate = LocalDateTime.now();
	}

	public Order(LineItem... items) {
		this(List.of(items), null);
	}

	Order() {
		this(new LineItem[0]);
	}

	public MonetaryAmount getPrice() {

		return lineItems.stream().//
				map(LineItem::getPrice).//
				reduce(MonetaryAmount::add).orElse(Money.of(0.0, "EUR"));
	}

	/**
	 * Marks the {@link Order} as payed.
	 */
	public Order markPaid() {

		if (isPaid()) {
			throw new IllegalStateException("Already paid order cannot be paid again!");
		}

		this.status = Status.PAID;

		registerEvent(new OrderPaid(getId()));

		return this;
	}

	public Order markInPreparation() {

		if (this.status != Status.PAID) {
			throw new IllegalStateException(
					String.format("Order must be in state payed to start preparation! Current status: %s", this.status));
		}

		this.status = Status.PREPARING;

		return this;
	}

	public Order markPrepared() {

		if (this.status != Status.PREPARING) {
			throw new IllegalStateException(String
					.format("Cannot mark Order prepared that is currently not preparing! Current status: %s.", this.status));
		}

		this.status = Status.READY;

		return this;
	}

	public Order markTaken() {

		if (this.status != Status.READY) {
			throw new IllegalStateException(
					String.format("Cannot mark Order taken that is currently not paid! Current status: %s.", this.status));
		}

		this.status = Status.TAKEN;

		return this;
	}


	public boolean isPaid() {
		return !this.status.equals(Status.PAYMENT_EXPECTED);
	}

	public boolean isReady() {
		return this.status.equals(Status.READY);
	}

	public boolean isTaken() {
		return this.status.equals(Status.TAKEN);
	}

	/**
	 * Enumeration for all the statuses an {@link Order} can be in.
	 */
	public enum Status {

		/**
		 * Placed, but not payed yet. Still changeable.
		 */
		PAYMENT_EXPECTED,

		/**
		 * Was payed. No changes allowed to it anymore.
		 */
		PAID,

		/**
		 * Currently processed.
		 */
		PREPARING,

		/**
		 * Ready to be picked up by the customer.
		 */
		READY,

		/**
		 * Was completed.
		 */
		TAKEN
	}
}

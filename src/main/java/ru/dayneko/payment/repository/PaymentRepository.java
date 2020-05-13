package ru.dayneko.payment.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.dayneko.order.model.Order;
import ru.dayneko.payment.model.Payment;

import java.util.Optional;

/**
 * Repository interface to manage {@link Payment} instances.
 */
interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

	Optional<Payment> findByOrder(Order order);
}

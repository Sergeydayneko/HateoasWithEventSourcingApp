package ru.dayneko.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.dayneko.order.model.Order;
import ru.dayneko.order.representation.OrderProjection;

import java.util.List;

/**
 * Repository to manage {@link Order} instances.
 */
@RepositoryRestResource(excerptProjection = OrderProjection.class)
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

	/**
	 * Returns all {@link Order}s with the given {@link Order.Status}.
	 *
	 * @param status must not be {@literal null}.
	 * @return
	 */
	List<Order> findByStatus(@Param("status") Order.Status status);
}

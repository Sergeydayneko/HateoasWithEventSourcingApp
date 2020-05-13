package ru.dayneko.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.dayneko.core.AbstractEntity;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineItem extends AbstractEntity {

    private final String name;
    private final int quantity;
    private final Milk milk;
    private final Size size;
    private final MonetaryAmount price;

    public LineItem(String name, MonetaryAmount price) {
        this(name, 1, Milk.SEMI, Size.LARGE, price);
    }
}

package ru.dayneko.config.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import ru.dayneko.core.AbstractAggregateRoot;
import ru.dayneko.order.model.*;
import ru.dayneko.payment.model.CreditCard;
import ru.dayneko.payment.model.CreditCardNumber;

import javax.money.MonetaryAmount;
import java.util.Collection;
import java.util.List;

class ShopModule extends SimpleModule {
    ShopModule() {

        setMixInAnnotation(AbstractAggregateRoot.class, AggregateRootMixin.class);
        setMixInAnnotation(Order.class, ShopModule.OrderMixin.class);
        setMixInAnnotation(LineItem.class, LineItemMixin.class);
        setMixInAnnotation(CreditCard.class, CreditCardMixin.class);
        setMixInAnnotation(CreditCardNumber.class, CreditCardNumberMixin.class);
    }

    static abstract class AggregateRootMixin {

        @JsonIgnore
        abstract List<Object> getDomainEvents();
    }

    @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    static abstract class OrderMixin {

        @JsonCreator
        public OrderMixin(Collection<LineItem> lineItems, Location location) {}
    }

    static abstract class LineItemMixin {

        @JsonCreator
        public LineItemMixin(String name, int quantity, Milk milk, Size size, MonetaryAmount price) {}
    }

    @JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
    static abstract class CreditCardMixin { }

    @JsonSerialize(using = ToStringSerializer.class)
    static abstract class CreditCardNumberMixin { }
}

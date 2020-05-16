package ru.dayneko.config.jackson.instantiator;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.javamoney.moneta.Money;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;

public class MoneyInstantiator extends ValueInstantiator {

    @Override
    public String getValueTypeDesc() {
        return MonetaryAmount.class.toString();
    }

    @Override
    public boolean canCreateFromString() {
        return true;
    }

    @Override
    public Object createFromString(DeserializationContext context, String value) {
        return Money.parse(value, MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()));
    }
}

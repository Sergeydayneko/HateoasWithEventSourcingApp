package ru.dayneko.config.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.dayneko.config.jackson.instantiator.MoneyInstantiator;
import ru.dayneko.config.jackson.serializer.MonetaryAmountSerializer;

import javax.money.MonetaryAmount;

class MoneyModule extends SimpleModule {
    MoneyModule() {
        addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
        addValueInstantiator(MonetaryAmount.class, new MoneyInstantiator());
    }
}

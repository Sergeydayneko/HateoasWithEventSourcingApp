package ru.dayneko.config;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

/**
 * {@link CurrencyUnit} constants
 */
public interface Currencies {

	CurrencyUnit EURO = Monetary.getCurrency("EUR");
}

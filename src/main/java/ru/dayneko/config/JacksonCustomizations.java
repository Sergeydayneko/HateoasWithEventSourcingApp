package ru.dayneko.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.javamoney.moneta.Money;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.json.JsonSchema.JsonSchemaProperty;
import org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer;
import org.springframework.data.util.TypeInformation;
import ru.dayneko.core.AbstractAggregateRoot;
import ru.dayneko.order.model.*;
import ru.dayneko.payment.model.CreditCard;
import ru.dayneko.payment.model.CreditCardNumber;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Configures custom serialization and deserialization of {@link Money} instances
 */
@Configuration
class JacksonCustomizations {

	@Bean
	public Module moneyModule() {
		return new MoneyModule();
	}

	@Bean

	public Module restbucksModule() {
		return new RestbucksModule();
	}

	@SuppressWarnings("serial")
	static class RestbucksModule extends SimpleModule {

		public RestbucksModule() {

			setMixInAnnotation(AbstractAggregateRoot.class, AggregateRootMixin.class);
			setMixInAnnotation(Order.class, RestbucksModule.OrderMixin.class);
			setMixInAnnotation(LineItem.class, LineItemMixin.class);
			setMixInAnnotation(CreditCard.class, CreditCardMixin.class);
			setMixInAnnotation(CreditCardNumber.class, CreditCardNumberMixin.class);
		}

		static abstract class AggregateRootMixin {
			abstract @JsonIgnore
            List<Object> getDomainEvents();
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
		static abstract class CreditCardMixin {}

		@JsonSerialize(using = ToStringSerializer.class)
		static abstract class CreditCardNumberMixin {}
	}

	@SuppressWarnings("serial")
	static class MoneyModule extends SimpleModule {

		public MoneyModule() {

			addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
			addValueInstantiator(MonetaryAmount.class, new MoneyInstantiator());
		}

		/**
		 * A dedicated serializer to render {@link MonetaryAmount} instances as formatted {@link String}. Also implements
		 * {@link JsonSchemaPropertyCustomizer} to expose the different rendering to the schema exposed by Spring Data REST.
		 */
		static class MonetaryAmountSerializer extends StdSerializer<MonetaryAmount>
				implements JsonSchemaPropertyCustomizer {

			private static final Pattern MONEY_PATTERN;

			static {

				String builder = "(?=.)^" + // Start
						"[A-Z]{3}?" + // 3-digit currency code
						"\\s" + // single whitespace character
						"(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?" + // digits with optional grouping by "," every 3)
						"(\\.[0-9]{1,2})?$";

				MONEY_PATTERN = Pattern.compile(builder);// End with a dot and two digits
			}

			public MonetaryAmountSerializer() {
				super(MonetaryAmount.class);
			}

			@Override
			public void serialize(MonetaryAmount value, JsonGenerator jgen, SerializerProvider provider) throws IOException {

				if (value != null) {
					jgen.writeString(MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()).format(value));
				} else {
					jgen.writeNull();
				}
			}

			@Override
			public JsonSchemaProperty customize(JsonSchemaProperty property, TypeInformation<?> type) {
				return property.withType(String.class).withPattern(MONEY_PATTERN);
			}
		}

		static class MoneyInstantiator extends ValueInstantiator {

			@Override
			public String getValueTypeDesc() {
				return MonetaryAmount.class.toString();
			}

			@Override
			public boolean canCreateFromString() {
				return true;
			}


			@Override
			public Object createFromString(DeserializationContext context, String value) throws IOException {
				return Money.parse(value, MonetaryFormats.getAmountFormat(LocaleContextHolder.getLocale()));
			}
		}
	}
}

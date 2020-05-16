package ru.dayneko.config.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.json.JsonSchema;
import org.springframework.data.rest.webmvc.json.JsonSchemaPropertyCustomizer;
import org.springframework.data.util.TypeInformation;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.io.IOException;
import java.util.regex.Pattern;

public class MonetaryAmountSerializer extends StdSerializer<MonetaryAmount> implements JsonSchemaPropertyCustomizer {

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
    public JsonSchema.JsonSchemaProperty customize(JsonSchema.JsonSchemaProperty property, TypeInformation<?> type) {
        return property.withType(String.class).withPattern(MONEY_PATTERN);
    }
}

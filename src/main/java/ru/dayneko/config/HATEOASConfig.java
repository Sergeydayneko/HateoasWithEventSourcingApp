package ru.dayneko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;

import static ru.dayneko.config.Constants.CURIE_NAMESPACE;

@Configuration
public class HATEOASConfig {

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider(CURIE_NAMESPACE, UriTemplate.of("/docs/{rel}.html"));
    }

}

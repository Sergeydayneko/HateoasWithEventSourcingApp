package ru.dayneko.core;

import lombok.Getter;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AbstractAggregateRoot extends AbstractEntity {

    /**
     * Throw DomainEvent event for further listening
     * This class refers only to Spring Data
     */
    @Getter(onMethod_ = { @DomainEvents })
    private transient final List<Object> domainEvents = new ArrayList<>();

    protected <T> T registerEvent(@NotNull T event) {

        this.domainEvents.add(event);

        return event;
    }

    /**
     * Clear DomainEvent currently held
     */
    @AfterDomainEventPublication
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}

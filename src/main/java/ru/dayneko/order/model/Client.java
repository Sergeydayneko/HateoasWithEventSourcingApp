package ru.dayneko.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/* Value object of client */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Client {
    private String name;
}

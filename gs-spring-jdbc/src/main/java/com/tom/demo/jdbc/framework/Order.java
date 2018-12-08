package com.tom.demo.jdbc.framework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Order {
    private String propertyName;
    private boolean ascending;

    public static Order asc(String propertyName) {
        return new Order(propertyName, true);

    }

    public static Order desc(String propertyName) {
        return new Order(propertyName, false);

    }

    @Override
    public String toString() {
        return propertyName + ' ' + (ascending ? "asc" : "desc");
    }
}

package com.tom;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SuperUser {
    int age;

    @Override
    public String toString() {
        return "SuperUser{" +
                "age=" + age +
                '}';
    }
}

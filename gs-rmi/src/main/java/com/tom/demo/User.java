package com.tom.demo;

import java.io.IOException;

public class User {
    private int age;

    /**
     * Getter for property 'age'.
     *
     * @return Value for property 'age'.
     */
    public int getAge() throws IOException {
        return age;
    }

    /**
     * Setter for property 'age'.
     *
     * @param age Value to set for property 'age'.
     */
    public void setAge(int age) {
        this.age = age;
    }
}

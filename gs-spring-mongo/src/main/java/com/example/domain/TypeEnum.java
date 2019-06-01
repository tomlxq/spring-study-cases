package com.example.domain;

public enum TypeEnum {
    STUDENT("student", "学生"),TEACHER("teacher", "教师");
    private final String value;
    private final String text;

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    TypeEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }


}

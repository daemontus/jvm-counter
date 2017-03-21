package com.github.daemontus;

public class TestClass {

    private int intField = 10;

    protected TestClass(int value) {
        intField = value;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int value) {
        intField = value;
    }
}

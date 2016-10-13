package com.jr.practice.utils;

/**
 * Created by Administrator on 2016-09-02.
 */

public class Cat {
    private String color;

    public Cat(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "color='" + color + '\'' +
                '}';
    }
}

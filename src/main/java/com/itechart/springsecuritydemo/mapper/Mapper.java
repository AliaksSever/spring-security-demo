package com.itechart.springsecuritydemo.mapper;

public interface Mapper<F, T> {

    T map(F object);
}

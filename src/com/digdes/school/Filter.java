package com.digdes.school;

public interface Filter {
    <T extends Comparable<? super T>> boolean applyFilter (T condition, T value);
}

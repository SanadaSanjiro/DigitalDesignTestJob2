package com.digdes.school.parser;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

// ENUM, задающий допустимые наименования столбцов и типы их значений
enum Column {
    ID(Long.class, Long::parseLong),
    LASTNAME(String.class, x-> x),
    AGE(Long.class, Long::parseLong),
    COST(Double.class, Double::parseDouble),
    ACTIVE(Boolean.class, Boolean::valueOf);

    private final Class<? extends Comparable<?>> columnClass;
    private final Function<String, Object> function;

    private static final Map<String, Column> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    Column (Class<? extends Comparable<?>> columnClass, Function<String, Object> f) {
        this.columnClass = columnClass;
        this.function = f;
    }

    Class<?> getColumnClass() {
        return this.columnClass;
    }

    static Optional<Column> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    // Преобразует строку в объект с типом данных столбца
    Object apply(String val) {
        return function.apply(val);
    }

    // Приводит объект к типу, соответствующему типу данных столбца
    static <T extends Comparable<? super T>> T castValue(Column column, Object val) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) column.getColumnClass();
        return type.cast(val);
    }
}

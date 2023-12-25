package com.digdes.school;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Column {
    ID(Long.class),
    LASTNAME(String.class),
    AGE(Long.class),
    COST(Double.class),
    ACTIVE(Boolean.class);

    private final Class columnClass;

    private static final Map<String, Column> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    Column(Class columnClass) {
        this.columnClass = columnClass;
    }

    public Class getColumnClass() {
        return this.columnClass;
    }

    public static Optional<Column> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    public static Object parseValue(Column c, String val) {
        Object obj;
        switch (c) {
            case ID, AGE -> obj = Long.parseLong(val);
            case LASTNAME -> obj = val;
            case COST -> obj = Double.parseDouble(val);
            case ACTIVE -> obj = Boolean.valueOf(val);
            default -> throw new IllegalArgumentException("Неверное значение параметра "
                    + val + " для столбца " + c);
        }
        return obj;
    }
}

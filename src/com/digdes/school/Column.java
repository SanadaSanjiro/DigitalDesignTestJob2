package com.digdes.school;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Column {
    ID("id", Long.class),
    LASTNAME("lastName", String.class),
    AGE("age", Long.class),
    COST("cost", Double.class),
    ACTIVE("active", Boolean.class);

    private final String columnName;
    private final Class columnClass;

    private static final Map<String, Column> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    Column(String columnName, Class columnClass) {
        this.columnName = columnName;
        this.columnClass = columnClass;
    }

    public Class getColumnClass() {
        return this.columnClass;
    }

    public static Optional<Column> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s));
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
    @Override
    public String toString() {
        return columnName;
    }


}

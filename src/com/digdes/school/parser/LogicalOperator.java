package com.digdes.school.parser;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

enum LogicalOperator {
    AND,
    OR;
    private static final Map<String, LogicalOperator> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    static Optional<LogicalOperator> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }
}

package com.digdes.school;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Operation {
    EQUALS("="),
    NOT_EQUALS("!="),
    LIKE("LIKE"),
    ILIKE("ILIKE"),
    MORE_OR_EQUALS(">="),
    LESS_OR_EQUAL("<="),
    MORE(">"),
    LESS("<");

    private String operationString;

    Operation (String s) {
        this.operationString = s;
    }

    private static final Map<String, Operation> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    public static Optional<Operation> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s));
    }


    @Override
    public String toString() {
        return operationString;
    }
}

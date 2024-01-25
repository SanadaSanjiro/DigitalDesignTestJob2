package com.digdes.school;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum LogicalFilter implements Filter {
    EQUALS("=") {
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)==0;
        }
    },
    NOT_EQUALS("!=") {
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            return Objects.isNull(value) || value.compareTo(condition)!=0;
        }
    },
    LIKE("LIKE") {
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            if (Objects.isNull(value)) return false;
            String c = (String) condition;
            String v = (String) value;
            WildcardType wt = WildcardType.getWildcardType(c);
            return WildcardType.matches(wt, c,v);
        }
    },
    ILIKE("ILIKE") {
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            if (Objects.isNull(value)) return false;
            String c = (String) condition;
            String v = (String) value;
            WildcardType wt = WildcardType.getWildcardType(c);
            return WildcardType.matches(wt, c.toLowerCase(), v.toLowerCase());
        }
    },
    MORE_OR_EQUALS(">="){
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)>=0;
        }
    },
    LESS_OR_EQUAL("<=") {
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)<=0;
        }
    },
    MORE(">"){
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)>0;
        }
    },
    LESS("<"){
        @Override
        public <T extends Comparable<? super T>> boolean applyFilter(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)<0;
        }
    };

    private final String OP_STRING;

    LogicalFilter(String s) {
        OP_STRING = s;
    }

    private static final Map<String, LogicalFilter> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    public static Optional<LogicalFilter> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s));
    }

    @Override
    public String toString() {
        return OP_STRING;
    }
}

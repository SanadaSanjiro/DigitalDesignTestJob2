package com.digdes.school.parser;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Перечисление, содержащее операции отношения и методы сравнения с их использованием
 */
enum RelationalOperator {
    EQUALS("=") {
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)==0;
        }
    },
    NOT_EQUALS("!=") {
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            return Objects.isNull(value) || value.compareTo(condition)!=0;
        }
    },
    LIKE("LIKE") {
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            if (Objects.isNull(value)) return false;
            String c = (String) condition;
            String v = (String) value;
            WildcardType wt = WildcardType.getWildcardType(c);
            return WildcardType.matches(wt, c,v);
        }
    },
    ILIKE("ILIKE") {
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            if (Objects.isNull(value)) return false;
            String c = (String) condition;
            String v = (String) value;
            WildcardType wt = WildcardType.getWildcardType(c);
            return WildcardType.matches(wt, c.toLowerCase(), v.toLowerCase());
        }
    },
    MORE_OR_EQUALS(">="){
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)>=0;
        }
    },
    LESS_OR_EQUALS("<=") {
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)<=0;
        }
    },
    MORE(">"){
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)>0;
        }
    },
    LESS("<"){
        @Override
        public <T extends Comparable<? super T>> boolean check(T condition, T value) {
            return !Objects.isNull(value) && value.compareTo(condition)<0;
        }
    };

    // Строковое представление операции
    private final String OP_STRING;

    RelationalOperator(String s) {
        OP_STRING = s;
    }

    // Используется для преобразования строк в экземпляры перечисления
    private static final Map<String, RelationalOperator> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    /**
     * Преобразует строку в элемент перечисления
     * @param s Строковое представление перечисления. Может быть как в верхнем, так и в нижнем регистре
     * @return Возвращает объект перечисления (операцию отношения)
     */
    static Optional<RelationalOperator> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s));
    }

    @Override
    public String toString() {
        return OP_STRING;
    }

    /**
     * Абстрактный метод, описывающий сравнение двух значений с помощью экземпляра перечисления
     * @param condition условие, с которым выполняется сравнение
     * @param value значение, которое будет сравниваться с условием
     * @return возвращает TRUE, если условие выполнено
     * @param <T> класс, расширяющий интерфейс Comparable (предполагается один из типов данных,
     *           используемых в столбцах талбицы.
     */
    abstract <T extends Comparable<? super T>> boolean check(T condition, T value);
}

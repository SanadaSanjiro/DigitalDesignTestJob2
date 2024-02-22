package com.digdes.school.parser;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * В данном перечислении описываются допустимые наименования столбцов и типы их значений, а также предоставляются
 * методы преобразования строк в значения столбцов.
 */

enum Column {
    ID(Long.class, Long::parseLong),
    LASTNAME(String.class, x-> x),
    AGE(Long.class, Long::parseLong),
    COST(Double.class, Double::parseDouble),
    ACTIVE(Boolean.class, Boolean::valueOf);

    // Хранит тип данных столбца
    private final Class<? extends Comparable<?>> columnClass;

    // Функция преобразования строки в объект соответствующего столбцу типа
    private final Function<String, Object> function;

    // Используется для преобразования строк в значения перечисления
    private static final Map<String, Column> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    Column (Class<? extends Comparable<?>> columnClass, Function<String, Object> f) {
        this.columnClass = columnClass;
        this.function = f;
    }

    /**
     * Метод для получения типа данных, хранящихся в конкретном столбце
     * @return Возвращает класс данных столбца
     */
    Class<?> getColumnClass() {
        return this.columnClass;
    }

    /**
     * Преобразует строку в элемент перечисления
     * @param s Строковое представление перечисления. Может быть как в верхнем, так и в нижнем регистре
     * @return Возвращает объект перечисления (колонку таблицы)
     */
    static Optional<Column> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    /**
     * Преобразует строку значения в объект соответсвующегостолбцу типа
     * @param val Строковое представление значения стобца.
     * @return Объект (тип соответствует типу данных столбца)
     */
    // Преобразует строку в объект с типом данных столбца
    Object apply(String val) {
        return function.apply(val);
    }

    /**
     * Приводит объект к типу, соответствующему типу данных столбца
     * @param column Экземпляр столбца таблицы, предоставляющий тип данных, к которому нужно привести объект
     * @param val Значение столбца таблицы - Object
     * @return возвращает значение, приведенное к типу данных столбца таблицы
     * @param <T> тип данных определяется столбцом
     */
    static <T extends Comparable<? super T>> T castValue(Column column, Object val) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) column.getColumnClass();
        return type.cast(val);
    }
}

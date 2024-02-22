package com.digdes.school.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Промежуточный класс с данными сотрудника. Используется для парсинга строк, предоставляя заполненный Map
 * для последующей обработки и внесения в базу
 */
class User {
    private final Map<Column, Object> map;
    static class UserBuilder
    {
        private final Map<Column, Object> temporaryMap = new HashMap<>();
        UserBuilder() {
        }
        UserBuilder addColumn(Column column, Object object)
        {
            //помещаемый в map объект проверяется на соответствие типу столбца, если нет - исключение ClassCastException
            temporaryMap.put(column, column.getColumnClass().cast(object));
            return this;
        }
        User build() { return new User(this); }
    }
    Map<Column, Object> getMap() {
        return map;
    }
    private User() { throw new IllegalArgumentException("Wrong constructor call. Use Builder!"); }
    private User(UserBuilder builder) {
        this.map = builder.temporaryMap;
    }
}
package ru.digital.design;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final Map<Column, Object> map;
    public static class UserBuilder
    {
        private final Map<Column, Object> temporaryMap = new HashMap<>();
        public UserBuilder() {
            for (Column column : Column.values()) {
                temporaryMap.put(column, null);
            }
        }
        public UserBuilder addColumn(Column column, Object object) throws ClassCastException
        {
            //помещаемый в map объект проверяется на соответствие типу столбца, если нет - исключение ClassCastException
            temporaryMap.put(column, column.getColumnClass().cast(object));
            return this;
        }
        public User build() { return new User(this); }
    }
    private User() {
        throw new IllegalArgumentException("Wrong constructor call. Use Builder!");
    }
    private User(UserBuilder builder) {
        this.map = builder.temporaryMap;
    }
    public Map<Column, Object> getMap() {
        return map;
    }
}
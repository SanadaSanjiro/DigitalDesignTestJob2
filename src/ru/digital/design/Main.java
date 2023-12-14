package ru.digital.design;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        for (Column column : Column.values()) {
            System.out.println(column + " имеет значение "+ column.getColumnName()
                    + " и класс " + column.getColumnClass());
        }
        User user = new User.UserBuilder()
                .addColumn(Column.ID, Long.valueOf(1))
                .addColumn(Column.LASTNAME, "Вася")
                .addColumn(Column.AGE, Long.valueOf(30))
                .addColumn(Column.COST, Double.valueOf(2.5))
                .addColumn(Column.ACTIVE, Boolean.valueOf(true))
                .build();
        Map<Column, Object> map = user.getMap();
        for(Column column: Column.values()) {
            System.out.println(column + ": " + map.get(column));
        }
    }
}
package com.digdes.school;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Operation operation;
        for (Operation op: Operation.values()) {
            operation = Operation.fromString(op.toString()).get();
            System.out.println(operation.name() + ", " + operation);
        }

        Column column;
        for (Column с: Column.values()) {
            column = Column.fromString(с.toString()).get();
            System.out.println(column.name() + ", " + column);
        }

        /*String s;
        JavaSchoolStarter jsc = new JavaSchoolStarter();
        Scanner scanner = new Scanner(System.in);+
        while (true) {
            s = scanner.nextLine();
            try {
                jsc.execute(Objects.requireNonNull(s.toString(), "Пустой аргумент!"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (s.trim().toLowerCase().equals("exit"))
                break;
        }*/
        /*
        User user = new User.UserBuilder()
                //.addColumn(Column.ID, Long.valueOf(1))
                .addColumn(Column.LASTNAME, "Вася")
                .addColumn(Column.AGE, Long.valueOf(30))
                .addColumn(Column.COST, Double.valueOf(2.5))
                .addColumn(Column.ACTIVE, Boolean.valueOf(true))
                .build();
        Map<Column, Object> map = user.getMap();
        for(Column column: Column.values()) {
            System.out.println(column + ": " + map.get(column));
        }*/
    }
}
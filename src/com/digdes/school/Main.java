package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс для тестирования основного кода.
 * Не требуется для корректной работы остальных классов.
 * Вносит в базу несколько тестовых записей и позволяет работать с ними с помощью команд в консоли
 */
public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter jvs = new JavaSchoolStarter();

        //Тестовые данные
        String[] requests = { "Insert VALUES  'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=TRUE",
                              "Insert VALuES  'lastName' = 'Петров' , 'id'=3, 'age'=30, 'active'=FALSE, 'cost'=10.1",
                              "Insert VALuES  'lastName' = 'Васечкин' , 'id'=2, 'age'=20, 'active'=false, 'cost' = 0.5",
                              "Insert VALuES   'lastName'  =  'Иванов'    , 'age'=35,   'active'=true,    'cost'=3.0 "};
        List<Map<String, Object>> list;
        for (String request: requests) {
            try {
                jvs.execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        select(jvs);

        // Принимает команды с консоли, построчно выводит результаты исполнения команды
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.nextLine();
            if (s.trim().equalsIgnoreCase("exit"))
                break;
            try {
                list = jvs.execute(Objects.requireNonNull(s, "Пустой аргумент!"));
                list.forEach(System.out::println);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Методя для вывода всего содержимого базы на консоль
    private static void select (JavaSchoolStarter jvs) {
        try {
            List<Map<String, Object>> list;
            list = jvs.execute("select");
            list.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
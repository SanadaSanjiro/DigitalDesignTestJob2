package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс для тестирования основного кода.
 * Не требуется для корректной работы остальных классов.
 */
public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter jvs = new JavaSchoolStarter();
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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.nextLine();
            try {
                list = jvs.execute(Objects.requireNonNull(s, "Пустой аргумент!"));
                list.forEach(System.out::println);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (s.trim().equalsIgnoreCase("exit"))
                break;
        }

        /*
        System.out.println(System.lineSeparator());
        System.out.println("В базу добавлены следующие позиции:");
        select(jvs);
        try {
            String query = "update values 'lastname' = 'пидорас' where 'active' = true and 'id' > 5 or 'lastname' iLike '%ов' or 'age' <= 40 and 'cost' >10";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
            System.out.println("Список после обновления: ");
            select(jvs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            String query = "select where 'id' != 0";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
            System.out.println("Список после обновления: ");
            select(jvs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        try {
            String query = "update values 'id' = null, 'lastname' = null, 'active' = null";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
            System.out.println("Список после обновления: ");
            select(jvs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        try {
            String query = "Insert VALUES  'lastName' = null , 'id'=3, 'age'=40, 'active'=TRUE";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
            System.out.println("Список после обновления: ");
            select(jvs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        /*
        list.forEach(m-> System.out.println(m));
        List<Block> blocks =  Parser.parseBlocks("'lastName' like '%ин'");
        list = Processor.filterList(list, blocks.get(0));
        System.out.println("Processed list:");
        list.forEach(m-> System.out.println(m));

        try {
            list=jvs.execute("delete");
            list.forEach(m-> System.out.println(m));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

         try {
            String query = "select where 'age' = 35 or 'id' = 3";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        try {
            String query = "delete where 'lastname' LIKE '%сечки%' and 'active' = false and 'id' = 2";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
            System.out.println("Список после удаления: ");
            select(jvs);

            query = "delete where 'id' = 3";
            System.out.println("Результат запроса: " + query);
            list = jvs.execute(query);
            list.forEach(System.out::println);
            System.out.println("Список после удаления: ");
            select(jvs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        System.out.println(Parser.whereParser("WHERE 'age'>=30 and 'id'=3"));

        int[] numbers = {1,2,3,4,5};
        for(LogicalFilter filter : LogicalFilter.values()) {
            for (int i : numbers) {
                try {
                    boolean b = filter.applyFilter(3, i);
                    System.out.println(i + filter.toString() + "3: " + b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        double[] doubles = {0.1,0.2,0.3,0.4,0.5};
        for(LogicalFilter filter : LogicalFilter.values()) {
            for (double d : doubles) {
                try {
                    boolean b = filter.applyFilter(.3, d);
                    System.out.println(d + filter.toString() + ": " + b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        String[] testStings = {
                "%Жопа 123%",
                "Жопа 123%",
                "%Жопа 123",
                "Жопа 123"
        };

        String[] values = {
                "Жопа 123",
                "абыр Жопа 123",
                "Жопа 123 абыр",
                "абыр Жопа 123 абыр"
        };
        WildcardType wc;
        for (String s : testStings) {
            wc = WildcardType.getWildcardType(s);
            System.out.println("Условие = " + s);
            for (String v : values) {
                System.out.println("Проверка строки " + v + ": " +
                        wc.matches(s, v));
            }
        }
        String string = "'lastName' = 'Федоров' " +
                "and 'lastName' ilike '%п%' oR 'active'=true  ";
        System.out.println(string);
        List<Condition> list =  Parser.whereParser(string);
        list.forEach(c -> System.out.println(c));
        string = "'lastName' = 'Федоров' ";
        System.out.println(string);
        list =  Parser.whereParser(string);
        list.forEach(c -> System.out.println(c));

        List<Block> blocks =  Parser.valuesParser("'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true");
        blocks.forEach(b-> System.out.println(b));**/
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
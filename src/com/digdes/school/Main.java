package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter jvs = new JavaSchoolStarter();
        String request = "Insert VALuES  'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true";
        try {
            List<Map<String, Object>> list = jvs.execute(request);
            list.forEach(m-> System.out.println(m));
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
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
        blocks.forEach(b-> System.out.println(b));*/

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
package ru.digital.design;

public class Main {
    public static void main(String[] args) {
        for (Columns coulmn : Columns.values()) {
            System.out.println(coulmn + " имеет значение "+ coulmn.getColumnName());
        }
        User user = new User.UserBulder().id(1).lastName("Вася").age(30).cost(3.5).active(true)
                .build();
        System.out.println(user);
        user = new User.UserBulder().id(2).lastName("Петя").build();
        System.out.println(user);
    }
}
package ru.digital.design;

public class Main {
    public static void main(String[] args) {
        for (Columns coulmn : Columns.values()) {
            System.out.println(coulmn + " имеет значение "+ coulmn.getColumnName());
        }
    }
}
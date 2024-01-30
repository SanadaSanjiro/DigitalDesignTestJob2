package com.digdes.school;

import java.util.EnumMap;
import java.util.Map;

// Задает виды дополнительных операторов в запросах
public enum SubQuery {
    VALUES {
        public String process (String s) {
            String[] args = s.trim().split(" ");
            if (!args[0].equalsIgnoreCase(this.toString())) //Проверка что строка начинается с VALUES
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует VALUES");
            return getArgsString(args);
        }
    },
    WHERE {
        public String process(String s) {
            String[] args = s.trim().split(" ");
            if (!args[0].equalsIgnoreCase(this.toString())) //Проверка что строка начинается с WHERE
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует WHERE");
            return getArgsString(args);
        }
    };

    //Метод возвращает Map с имеющимися дополнительными операторами и строки с соответствующими им запросами
    public static Map<SubQuery, String> processSubQuery(String query) {
        query=query.trim();
        Map<SubQuery, String> result = new EnumMap<>(SubQuery.class);
        int whereStart = query.toUpperCase().indexOf(WHERE.toString());
        if (!(whereStart==-1)) {
            result.put(WHERE, WHERE.process(query.substring(whereStart)));
            query=query.substring(0, whereStart);
        }
        if (query.toUpperCase().startsWith(VALUES.toString())) {
            result.put(VALUES, VALUES.process(query));
        }
        return result;
    }

    //Пересобираем аргументы, убирая лишние пробелы
    private static String getArgsString(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i<s.length; i++) {
            sb.append(s[i]).append(" ");
        }
        return sb.toString();
    }

    abstract String process (String s);
}

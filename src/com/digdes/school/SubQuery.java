package com.digdes.school;

import java.util.EnumMap;
import java.util.Map;

// Задает виды дополнительных операторов в запросах
public enum SubQuery implements Subcommand {
    VALUES {
        public String process (String s) {
            String[] args = s.trim().split(" ");
            if (!args[0].toUpperCase().equals(this.toString()))
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует VALUES");
            return Parser.getArgsString(args);
        }
    },
    WHERE {
        public String process(String s) {
            String[] args = s.trim().split(" ");
            if (!args[0].toUpperCase().equals(this.toString()))
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует WHERE");
            return Parser.getArgsString(args);
        }
    };

    //Метод возвращает Map с имеющимися дополнительными операторами и строки с соответствующими им запросами
    public static Map<SubQuery, String> processSubQuery(String query) {
        query=query.trim();
        Map<SubQuery, String> result = new EnumMap<>(SubQuery.class);
        int whereStart = query.toUpperCase().indexOf(WHERE.toString());
        if (whereStart==-1) {
            result.put(WHERE, WHERE.process(query.substring(whereStart)));
            query=query.substring(0, whereStart);
        }
        if (query.toUpperCase().startsWith(VALUES.toString())) {
            result.put(VALUES, VALUES.process(query));
        }
        return result;
    }
}

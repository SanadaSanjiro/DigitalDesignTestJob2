package com.digdes.school;

import java.util.List;
import java.util.Map;

public enum SubQuery implements Subcommand {
    VALUES {
        public String process (String s) {
            System.out.println(s);
            String[] args = s.trim().split(" ");
            if (!args[0].toUpperCase().equals(this.toString()))
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует VALUES");
            return Parser.getArgsString(args);
        }
    },
    WHERE {
        public String process(String s) {
            return s;
        }
    }
}

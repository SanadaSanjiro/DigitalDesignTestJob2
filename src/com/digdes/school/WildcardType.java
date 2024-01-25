package com.digdes.school;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Класс содержит типы регулярных выражений для обработки подстановочных знаков в строковых фильтрах
public enum WildcardType {
    BOTH("%(.*)%", ".*", ".*"),
    LEFT("%(.*[^%])", ".*", ""),
    RIGHT("([^%].*)%", "", ".*"),
    NONE("([^%].*[^%])", "", "");

    private final String REGEXP, PREFIX, SUFFIX;
    WildcardType (String regexp, String prefix, String suffix) {
        REGEXP = regexp;
        PREFIX = prefix;
        SUFFIX = suffix;
    }
    //Метод возвращает enum, соответствующий строке запроса
    public static WildcardType getWildcardType(String condition) {
        condition = condition.replaceAll("'", "");
        for(WildcardType wt : WildcardType.values()) {
            if (condition.matches(wt.REGEXP)) return wt;
        }
        throw new IllegalArgumentException("Неверный формат запроса строкового параметра");
    }
    //Метод возвращает строковый шаблон, который будет использован для поиска строк
    private String getPattern(String string) {
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Неверный формат запроса строкового параметра");
    }

    public String getPrefix() {
        return PREFIX;
    }

    public String getSuffix() {
        return SUFFIX;
    }

    //Метод проверяет соответствие поступившей строки шаблону, заданному в данном enum
    public static boolean matches(WildcardType wc,String condition, String value) {
        condition = condition.replaceAll("%", "").replaceAll("'", "");
        value = value.replaceAll("'", "");
        String rx = wc.getPrefix() + condition + wc.getSuffix();
        return value.matches(rx);
    }
}

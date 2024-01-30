package com.digdes.school.parser;

//Класс содержит типы регулярных выражений для обработки подстановочных знаков в строковых фильтрах
enum WildcardType {
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
    static WildcardType getWildcardType(String condition) {
        condition = condition.replaceAll("'", "");
        for(WildcardType wt : WildcardType.values()) {
            if (condition.matches(wt.REGEXP)) return wt;
        }
        throw new IllegalArgumentException("Неверный формат запроса строкового параметра");
    }

    private String getPrefix() {
        return PREFIX;
    }

    private String getSuffix() {
        return SUFFIX;
    }

    //Метод проверяет соответствие поступившей строки шаблону, заданному в данном enum
    static boolean matches(WildcardType wc,String condition, String value) {
        condition = condition.replaceAll("%", "").replaceAll("'", "");
        value = value.replaceAll("'", "");
        String rx = wc.getPrefix() + condition + wc.getSuffix();
        return value.matches(rx);
    }
}

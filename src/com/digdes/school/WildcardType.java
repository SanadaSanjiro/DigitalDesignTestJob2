package com.digdes.school;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static WildcardType getWildcardType(String condition) {
        for(WildcardType wt : WildcardType.values()) {
            if (condition.matches(wt.REGEXP)) return wt;
        }
        throw new IllegalArgumentException("Неверный формат запроса строкового параметра");
    }
    private String getPattern(String string) {
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(string);
        matcher.matches();
        String s = matcher.group(1);
        return s;
    }

    public boolean matches(String request, String value) {
        String rx = PREFIX + getPattern(request) + SUFFIX;
        return value.matches(rx);
    }
}

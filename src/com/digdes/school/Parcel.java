package com.digdes.school;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parcel {
    private static Set<Character> columnCharSet = getChars(Column.class);
    private static Set<Character> operationCharSet = getChars(Operation.class);
    private static Set<Character> queryCharSet = getChars(Query.class);
    private static Set<Character> subCommandCharSet = getChars(Subcommand.class);
    private static final String VALUES_PATTERN = "'?(([\\d]+\\.?[\\d]*)|([%\\wа-яА-ЯёЁ-]+)|" +
            "(FALSE)|(TRUE))'?";
    private static final String FULL_PATTERN = " *'("
            + createRegexp(columnCharSet) + "*)' *("
            + createRegexp(operationCharSet) + "*) *"
            + VALUES_PATTERN + " *";
    private static Pattern blockPattern = Pattern.compile(FULL_PATTERN);
    public static User valueParcel(String string) {
        Matcher matcher = blockPattern.matcher(string);
        System.out.println(matcher);
        boolean isMatches = matcher.matches();
        System.out.println(isMatches);
        if (isMatches) {
            String column = matcher.group(1);
            String operation = matcher.group(2);
            String values = matcher.group(3);
            System.out.println(column);
            System.out.println(operation);
            System.out.println(values);
        } else throw new IllegalArgumentException("Пошли в жопу!");
        return null;
    }

    private static <T extends Enum> Set<Character> getChars(Class<T> enumClass) {
        Enum[] enumConstants = enumClass.getEnumConstants();
        Set<Character> chars = new HashSet<>();
        String lowCase;
        String upperCase;
        for (Enum e :enumConstants) {
            lowCase = e.toString().toLowerCase();
            upperCase = e.toString().toUpperCase();
            char[] characters = lowCase.toCharArray();
            for (char ch: characters) {
                chars.add(ch);
            }
            characters = upperCase.toCharArray();
            for (char ch: characters) {
                chars.add(ch);
            }
        }

        return chars;
    }

    private static String createRegexp(Set<Character> characters) {
        StringBuilder sb = new StringBuilder("[");
        characters.forEach(c->sb.append(c));
        sb.append("]");
        return sb.toString();
    }

    private static Column getColumn(String string) {
        Optional<Column> result = Column.fromString(string);
        if (result.isPresent()) return result.get();
        throw new IllegalArgumentException("Кривая жопа, блять!");
    }

    
}

package com.digdes.school;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Set<Character> columnCharSet = getChars(Column.class);
    private static Set<Character> operationCharSet = getChars(LogicalFilter.class);
    private static Set<Character> queryCharSet = getChars(Query.class);
    private static Set<Character> subCommandCharSet = getChars(SubQuery.class);
    private static Set<Character> logicalOpsCharSet = getChars(LogicalOperator.class);
    private static final String VALUES_PATTERN = "'?(([\\d]+\\.?[\\d]*)|([%\\wа-яА-ЯёЁ-]+)|" +
            "(FALSE)|(TRUE))'?";
    private static final String FULL_PATTERN = " *'("
            + createRegexp(columnCharSet) + "*)' *("
            + createRegexp(operationCharSet) + "*) *"
            + VALUES_PATTERN + " *";
    private static Pattern blockPattern = Pattern.compile(FULL_PATTERN);

    public static List<Block> valuesParser(String string) {
        String[] blocks = string.split(",");
        List<Block> values = new ArrayList<>();
        for (String s : blocks) {
            values.add(blockParser(s));
        }
        return values;
    }

    public static List<Condition> whereParser(String string) {
        List<Condition> conditions = new ArrayList<>();
        String logicalRegexp = " ?" + createRegexp(logicalOpsCharSet) + "+ ?";
        Pattern logicalPattern = Pattern.compile(logicalRegexp);
        Matcher logicalMatcher;
        Matcher blockMatcher = blockPattern.matcher(string);
        String substring;
        int start;
        int end;
        LogicalOperator lo;
        Optional<LogicalOperator> optional;
        Condition condition;

        while (blockMatcher.find()) {
            start = blockMatcher.start();
            end = blockMatcher.end();
            substring = string.substring(blockMatcher.start(), blockMatcher.end());
            Block block = blockParser(substring);
            string = string.replaceFirst(substring, "");
            logicalMatcher = logicalPattern.matcher(string);
            if (logicalMatcher.find()) {
                start = logicalMatcher.start();
                end = logicalMatcher.end();
                substring = string.substring(start, end);
                string = string.replaceFirst(substring, "");
                optional = LogicalOperator.fromString(substring.trim());
                lo = optional.get();
                condition = new Condition(lo, block);
                conditions.add(condition);
                string = string.replaceFirst(substring, "");
                }
            else {
                condition = new Condition(null, block);
                conditions.add(condition);
                return conditions;
                }
            blockMatcher = blockPattern.matcher(string);

        }
        return conditions;
    }

    public static String getArgsString(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i<s.length; i++) {
            sb.append(s[i]).append(" ");
        }
        return sb.toString();
    }

    private static Block blockParser(String string) {
        Matcher matcher = blockPattern.matcher(string);
        boolean isMatches = matcher.matches();
        Block block;
        if (isMatches) {
            String column = matcher.group(1);
            String operation = matcher.group(2);
            String values = matcher.group(3);
            Column c = getColumn(column);
            LogicalFilter op = getOperation(operation);
            Object val = getValue(c, values);
            block = new Block(c, op, val);
        } else throw new IllegalArgumentException("Пошли в жопу!");
        return block;
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
        throw new IllegalArgumentException("Неверный тип колонки " + string);
    }

    private static LogicalFilter getOperation(String string) {
        Optional<LogicalFilter> result = LogicalFilter.fromString(string.toUpperCase());
        if (result.isPresent()) return result.get();
        throw new IllegalArgumentException("Неверный тип операции " + string);
    }

    private static Object getValue(Column column, String string) {
        return Column.parseValue(column, string);
    }
}

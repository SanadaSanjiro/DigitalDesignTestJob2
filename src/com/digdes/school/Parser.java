package com.digdes.school;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private static final String VALUES_PATTERN = "(?i)'?((\\d+\\.?\\d*)|('[%\\wа-яА-ЯёЁ-]+')|" +
            "(FALSE)|(TRUE))'?";
    private static final String FULL_PATTERN = " *'("
            + getEnumPattern(Column.class) + "*)' *("
            + getEnumPattern(LogicalFilter.class) + "*) *"
            + VALUES_PATTERN + " *";
    private static final Pattern blockPattern = Pattern.compile(FULL_PATTERN);

    public static List<Block> parseBlocks(String string) {
        String[] blocks = string.split(",");
        List<Block> values = new ArrayList<>();
        for (String s : blocks) {
            values.add(getBlock(s));
        }
        return values;
    }

    public static List<Condition> whereParser(String string) {
        List<Condition> conditions = new ArrayList<>();
        String logicalRegexp = " ?" + getEnumPattern(LogicalOperator.class) + "+ ?";
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
            substring = string.substring(start, end);
            Block block = getBlock(substring);
            string = string.replaceFirst(substring, "");
            logicalMatcher = logicalPattern.matcher(string);
            if (logicalMatcher.find()) {
                start = logicalMatcher.start();
                end = logicalMatcher.end();
                substring = string.substring(start, end);
                optional = LogicalOperator.fromString(substring.trim());
                lo = optional.orElse(null);
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

    private static Block getBlock(String string) {
        Matcher matcher = blockPattern.matcher(string);
        boolean isMatches = matcher.matches();
        Block block;
        if (isMatches) {
            String column = matcher.group(1);
            String operation = matcher.group(2);
            String value = matcher.group(3);
            Column c = getColumn(column);
            LogicalFilter op = getOperation(operation);
            Object val = getValue(c, value);
            block = new Block(c, op, val);
        } else throw new IllegalArgumentException("Ошибка обработки блока команды: " + string);
        return block;
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
        return column.apply(string);
    }

    // Возвращают регулярное выражение со значениями наименований столбцов
    // для использования в качестве паттерна при обработке строк
    private static <T extends Enum<T>> String getEnumPattern (Class<T> c) {
        String values = Arrays.stream(c.getEnumConstants())
                .map(Enum::toString)
                .collect(Collectors.joining("||"));
        return "(?i)" + values; // (?i) - флаг "не учитывать регистр при поиске"
    }
}

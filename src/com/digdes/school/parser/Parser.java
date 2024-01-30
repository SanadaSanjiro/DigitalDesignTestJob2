package com.digdes.school.parser;

import com.digdes.school.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Parser {
    private static final String VALUES_PATTERN = "(?i)'?((\\d+\\.?\\d*)|('[%\\wа-яА-ЯёЁ-]+')|" +
            "(FALSE)|(TRUE))'?";
    private static final String FULL_PATTERN = " *'("
            + getEnumPattern(Column.class) + "*)' *("
            + getEnumPattern(RelationalOperator.class) + "*) *"
            + VALUES_PATTERN + " *";
    private static final Pattern BLOCK_PATTERN = Pattern.compile(FULL_PATTERN);

    public static Map<String, Object> stringToMap(String string) {
        String[] blocks = string.split(",");
        List<Block> values = new ArrayList<>();
        for (String s : blocks) {
            values.add(getBlock(s));
        }
        if (values.isEmpty())
            throw new IllegalArgumentException("Ошибка вставки. " +
                    "Данные не найдены.");
        User.UserBuilder userBuilder = new User.UserBuilder();
        for (Block block : values) {
            if (!block.getRelation().equals(RelationalOperator.EQUALS))
                throw new IllegalArgumentException("Ошибка вставки. " +
                        "Данные должны быть в формате 'поле' = значение");
            userBuilder.addColumn(block.getColumn(), block.getValue());
        }
        return userToMap(userBuilder.build());
    }

    public static List<Map<String, Object>> parseWhere(Storage storage, String string) {
        List<Condition> conditions = new ArrayList<>();
        String logicalRegexp = " ?" + getEnumPattern(LogicalOperator.class) + "+ ?";
        Pattern logicalPattern = Pattern.compile(logicalRegexp);
        Matcher logicalMatcher;
        Matcher blockMatcher = BLOCK_PATTERN.matcher(string);
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
                return getFilteredList(storage, conditions);
                }
            blockMatcher = BLOCK_PATTERN.matcher(string);
        }
        return getFilteredList(storage, conditions);
    }

    private static Block getBlock(String string) {
        Matcher matcher = BLOCK_PATTERN.matcher(string);
        boolean isMatches = matcher.matches();
        Block block;
        if (isMatches) {
            String column = matcher.group(1);
            String operation = matcher.group(2);
            String value = matcher.group(3);
            Column c = getColumn(column);
            RelationalOperator op = getOperation(operation);
            Object val = stringToValue(c, value);
            block = new Block(c, op, val);
        } else throw new IllegalArgumentException("Ошибка обработки блока команды: " + string);
        return block;
    }

    private static Column getColumn(String string) {
        Optional<Column> result = Column.fromString(string);
        if (result.isPresent()) return result.get();
        throw new IllegalArgumentException("Неверный тип колонки " + string);
    }

    private static RelationalOperator getOperation(String string) {
        Optional<RelationalOperator> result = RelationalOperator.fromString(string.toUpperCase());
        if (result.isPresent()) return result.get();
        throw new IllegalArgumentException("Неверный тип операции " + string);
    }

    // Преобразует строку в объект соответствуюшего столбцу типа
    private static Object stringToValue(Column column, String string) {
        return column.apply(string);
    }

    // Возвращают регулярное выражение со значениями наименований перечисления
    // для использования в качестве паттерна при обработке строк
    private static <T extends Enum<T>> String getEnumPattern (Class<T> c) {
        String values = Arrays.stream(c.getEnumConstants())
                .map(Enum::toString)
                .collect(Collectors.joining("||"));
        return "(?i)" + values; // (?i) - флаг "не учитывать регистр при поиске"
    }

    // Фильтрует полученный из хранилища список пользователей согласно списку условий
    private static List<Map<String, Object>> getFilteredList(Storage storage, List<Condition> conditions) {
        List<Map<String, Object>> list = storage.getAll();
        return Filter.applyConditions(list, conditions);
    }

    // Преобразует объект User в map для помещения в хранилище
    private static Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        user.getMap().forEach((key1, value) -> {
            var key = key1.toString();
            map.put(key, value);
        });
        return map;
    }
}

package com.digdes.school.parser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

enum LogicalOperator {
    AND {
        @Override
        public boolean apply(boolean cond1, boolean cond2) {
            return cond1 && cond2;
        }
    },
    OR {
        @Override
        public boolean apply(boolean cond1, boolean cond2) {
            return cond1 || cond2;
        }
    };
    private static final Map<String, LogicalOperator> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    static Optional<LogicalOperator> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    public abstract boolean apply(boolean cond1, boolean cond2);

    public static List<Map<String, Object>> applyConditions(List<Map<String, Object>> userList,
                                                     List<Condition> condList) {
        List<Map<String, Object>> result = userList.stream()
                .filter(user -> LogicalOperator.processChain(condList, user))
                .collect(Collectors.toList());
        return result;
    }

    private static boolean processChain( List<Condition> list, Map<String, Object> user) {
        if (list.isEmpty()) throw new IllegalArgumentException("Пустой список параметров!");
        Iterator<Condition> iterator = list.iterator();
        Condition condition = iterator.next();
        LogicalOperator lo;
        Block block = condition.getBlock();
        boolean isTrue = checkCondition(block, user);
        int counter = 0;

        while (true) {
            //System.out.println("Шаг обработки: " + ++counter + ". Условие: " + condition);
            lo = condition.getLogical();
            if (Objects.isNull(lo)) return isTrue;
            if (! iterator.hasNext()) throw new IllegalArgumentException("Пропущено условие в логическом операторе!");
            if (isTrue&&lo.equals(LogicalOperator.OR)) return true;
            condition = iterator.next();
            block = condition.getBlock();
            isTrue = lo.apply(isTrue, checkCondition(block, user));
        }
    }

    private static <T extends Comparable<T>>
    boolean checkCondition(Block block, Map<String, Object> user) {
        Column column = block.getColumn();
        T condition = Column.castValue(column, block.getValue());
        RelationalOperator relation = block.getRelation();
        T value = Column.castValue(column, user.get(column.toString()));
        return relation.check(condition, value);
    }
}

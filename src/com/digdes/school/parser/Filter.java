package com.digdes.school.parser;

import java.util.*;

import static java.util.Objects.isNull;

// Обеспечивает фильтрацию списка пользователей по списку условий
class Filter {
    static List<Map<String, Object>> applyConditions(List<Map<String, Object>> userList,
                                                            List<Condition> condList) {
        List<Map<String, Object>> result = new ArrayList<>();
        Iterator<Condition> iterator;
        for (Map<String, Object> user : userList) {
            iterator = condList.iterator();
            if (isCompliant(iterator, user)) {
                System.out.println("Пользователь " + user + " удовлетворяет условиям");
                result.add(user);
            }
        }
        return result;
    }

    private static boolean isCompliant(Iterator<Condition> iterator, Map<String, Object> user) {
        Condition condition = iterator.next();
        Block block = condition.getBlock();
        LogicalOperator lo = condition.getLogical();
        boolean result;
        if (Objects.isNull(lo)) {
            System.out.println("Для пользователя: " + user);
            result = checkCondition(block, user);
        } else {
            result = lo.apply(checkCondition(block, user), isCompliant(iterator, user));
        }
        System.out.println("результат обработки блока " + block + ": " + result);
        return result;
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

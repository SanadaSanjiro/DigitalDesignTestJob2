package com.digdes.school.parser;

import java.util.*;

// Обеспечивает фильтрацию списка пользователей по списку условий
class Filter {
    static List<Map<String, Object>> applyConditions(List<Map<String, Object>> userList,
                                                            List<Condition> condList) {
        Iterator<Condition> iterator = condList.iterator();
        return processLogical(userList, userList, iterator);
    }

    // Рекурсивно применяет цепочку фильтров к списку пользователей
    private static List<Map<String, Object>> processLogical(
            List<Map<String, Object>> allUsers,
            List<Map<String, Object>> filteredUsers,
            Iterator<Condition> iterator)
    {
        Condition condition = iterator.next();
        Block block = condition.getBlock();
        LogicalOperator lo = condition.getLogical();
        List<Map<String, Object>> result = filterList(filteredUsers, block);
        if (Objects.isNull(lo)) {
            return result;
        }
        if (lo.equals(LogicalOperator.OR)) {
            result = joinLists(filteredUsers, processLogical(allUsers, allUsers, iterator));
        }
        if (lo.equals(LogicalOperator.AND)) {
            result = processLogical(allUsers, result, iterator);
        }
        return result;
    }

    // Фильтрует список в соответствии с полученными оператором и значнеием
    private static <T extends Comparable<T>> List<Map<String, Object>>
    filterList(List<Map<String, Object>> list, Block block)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Column column = block.getColumn();
        T condition = Column.castValue(column, block.getValue());
        RelationalOperator relation = block.getRelation();
        for (Map<String, Object> map : list) {
            T value = Column.castValue(column, map.get(column.toString()));
            if (relation.check(condition, value)) result.add(map);
        }
        return result;
    }

    // Объединяет два списка без дублирования значений
    private static List<Map<String, Object>> joinLists(List<Map<String, Object>> list1, List<Map<String, Object>>list2)
    {
        for (Map<String, Object> map : list2) {
            if (!list1.contains(map)) {
                list1.add(map);
            }
        }
        return list1;
    }
}

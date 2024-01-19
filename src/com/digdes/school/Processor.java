package com.digdes.school;

import java.util.*;

// ENUM определяет перечень допустимых команд и способы их выполнения
public class Processor {
    public static <T extends Comparable<T>> List<Map<String, Object>>
    filterList(List<Map<String, Object>> list, Block block)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Column column = block.getColumn();
        T condition = Column.castValue(column, block.getValue());
        LogicalFilter filter = block.getFilter();
        for (Map<String, Object> map : list) {
            T value = Column.castValue(column, map.get(column.toString()));
            boolean b =  filter.applyFilter(condition, value);
            if (b) result.add(map);
        }
        return result;
    }

    public static <T extends Comparable<T>> List<Map<String, Object>>
    processLogicalList (List<Map<String, Object>> userList, List<Condition> condList) {
        Iterator<Condition> iterator = condList.iterator();
        return processLogical(userList, userList,iterator);
    }

    private static List<Map<String, Object>> processLogical(
            List<Map<String, Object>> fullList,
            List<Map<String, Object>> processedList,
            Iterator<Condition> iterator)
    {
        Condition condition = iterator.next();
        System.out.println("Логический процессор обрабатывает строку " + condition);
        Block block = condition.getBlock();
        LogicalOperator lo = condition.getLogical();
        List<Map<String, Object>> result = filterList(processedList, block);
        if (Objects.isNull(lo)) {
            return result;
        }
        if (lo.equals(LogicalOperator.OR)) {
            result.addAll(processLogical(fullList, fullList, iterator));
        }
        if (lo.equals(LogicalOperator.AND)) {
            result.addAll(processLogical(fullList, result, iterator));
        }
        return result;
    }
}

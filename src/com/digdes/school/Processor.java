package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// ENUM определяет перечень допустимых команд и способы их выполнения
public class Processor {
    public static <T extends Comparable<T>> List<Map<String, Object>> filterList(List<Map<String, Object>>
                                                                                         list, Block block)
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

        /*
        newList.addAll(list.stream()
                .filter(map -> (map.get(column)==null))
                .collect(Collectors.toList()));
        newList.addAll(list.stream()
                .filter(map -> (!(map.get(column)==null)))
                .filter(map -> (!((long) map.get(column) == condition)))
                .collect(Collectors.toList()));
        return newList;
         */
        return result;
    }
}

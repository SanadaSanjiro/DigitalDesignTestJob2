package com.digdes.school;

import jdk.dynalink.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Processor {
    public static <T extends Comparable<T>> List<Map<String, Object>> filterList(List<Map<String, Object>> list, Block block) {
        List<Map<String, Object>> result = new ArrayList<>();
        Column column = block.getColumn();
        Object condition = block.getValue();
        T processedCond = Column.castValue(column, condition);
        LogicalFilter logicalFilter = block.getOperation();
        for (Map<String, Object> map : list) {
            Object val =  map.get(column.toString());
            T processedVal = Column.castValue(column, val);
            boolean b =  logicalFilter.applyFilter(processedCond, processedVal);
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

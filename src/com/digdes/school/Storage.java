package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Storage {
    private final List<Map<String, Object>> list = new ArrayList<>();
    public List<Map<String, Object>> addRow(User user) {
        Map<String, Object> map = new HashMap<>();
        user.getMap().entrySet().forEach(e-> {
            var key = e.getKey().toString();
            var value = e.getValue();
            map.put(key, value);
            }
        );
        /*Map<String, Object> map = new HashMap<>();
        Map<Column, Object> userMap = user.getMap();
        for (Map.Entry<Column, Object> entry : userMap.entrySet()) {
            map.put(entry.getKey().getColumnName(), entry.getValue());
        }*/
        list.add(map);
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(map);
        return result;
    }
    public List<Map<String, Object>> get() {
        return list;
    }


}

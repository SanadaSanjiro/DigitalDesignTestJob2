package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    private final List<Map<String, Object>> list = new ArrayList<>();
    public List<Map<String, Object>> add(User user) {
        Map<String, Object> map = userToMap(user);
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(map);
        list.add(map);
        return result;
    }
    public List<Map<String, Object>> deleteRow (Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (!list.contains(map)) return result;
        result.add(map);
        list.remove(map);
        return result;
    }

    public List<Map<String, Object>> deleteAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        result.addAll(list);
        list.clear();
        return result;
    }

    public List<Map<String, Object>> getAll() {
        return list;
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        user.getMap().entrySet().forEach(e-> {
                    var key = e.getKey().toString();
                    var value = e.getValue();
                    map.put(key, value);
                }
        );
        return map;
    }
}

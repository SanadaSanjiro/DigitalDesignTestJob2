package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Storage {
    private final List<Map<String, Object>> list = new ArrayList<>();
    public List<Map<String, Object>> add(Map<String, Object> user) {
        List<Map<String, Object>> result = new ArrayList<>();
        list.add(user);
        result.add(user);
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
        List<Map<String, Object>> result = new ArrayList<>(list);
        list.clear();
        return result;
    }

    public List<Map<String, Object>> getAll() {
        return list;
    }


}

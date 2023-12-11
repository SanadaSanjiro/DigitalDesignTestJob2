package ru.digital.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Storage {
    private final List<Map<String, Object>> list = new ArrayList<>();
    public List<Map<String, Object>> addRow(Map<String, Object> row) {
        list.add(row);
        return list;
    }
    public List<Map<String, Object>> get() {
        return list;
    }


}

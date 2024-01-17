package com.digdes.school;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private final Storage storage;
    public JavaSchoolStarter() {
        storage = new Storage();
    }
    public List<Map<String,Object>> execute(String request) throws Exception {
        return Query.processQuery(storage, request);
    }
}

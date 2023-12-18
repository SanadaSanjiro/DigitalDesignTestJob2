package com.digdes.school;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private final Storage storage;
    public JavaSchoolStarter() {
        storage = new Storage();
    }
    public List<Map<String,Object>> execute(String request) throws Exception {
        String[] command = request.split(" ");
        System.out.println(Query.valueOf(command[0].toUpperCase()));
        for (String s : command) {
            System.out.println(s);
        }
        return storage.get();
    }
}

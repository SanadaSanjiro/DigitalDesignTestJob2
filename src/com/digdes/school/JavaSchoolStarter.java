package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JavaSchoolStarter {
    private final Storage storage;
    public JavaSchoolStarter() {
        storage = new Storage();
    }
    public List<Map<String,Object>> execute(String request) throws Exception {
        String[] command = request.split(" ");
        Optional<Query> q = Query.fromString(command[0]);
        if (q.isEmpty()) throw new IllegalArgumentException("Неопознанная команда");
        Query query = q.get();
        return query.execute(storage, Parser.getArgsString(command));
    }
}

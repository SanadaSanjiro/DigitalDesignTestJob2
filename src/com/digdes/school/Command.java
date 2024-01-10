package com.digdes.school;

import java.util.List;
import java.util.Map;

public interface Command {
    public List<Map<String,Object>> execute(Storage storage, String args);
}

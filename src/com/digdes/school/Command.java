package com.digdes.school;

import java.util.List;
import java.util.Map;

// Интерфей описывает запрос к базе данных
// Реализован в Enum Query
public interface Command {
    List<Map<String,Object>> execute(Storage storage, String args);
}

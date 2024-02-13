package com.digdes.school;

import com.digdes.school.parser.Parser;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Query {
    INSERT {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            if (!subQueries.containsKey(SubQuery.VALUES))     // Проверка на  наличие подкоманды VALUES
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                    "Отсутствует обязательный параметр VALUES");
            if (subQueries.containsKey(SubQuery.WHERE))       // И отсутсвие подкоманды Where
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                        "Параметр WHERE недопустим в данном контексте");
            args = subQueries.get(SubQuery.VALUES);
            Map<String, Object> user = Parser.stringToMap(args);
            for (Object value : user.values()) {
                if (Objects.isNull(value))
                    throw new IllegalArgumentException(
                            "Значение поля при добавлении пользователя не может быть null!");
            }
            return storage.add(Parser.stringToMap(args));
        }
    },
    DELETE {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            if (subQueries.containsKey(SubQuery.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Delete. " +
                        "Параметр VALUES недопустим в данном контексте");
            if (!subQueries.containsKey(SubQuery.WHERE)) return storage.deleteAll();
            List<Map<String, Object>> result = Parser.parseWhere(storage, subQueries.get(SubQuery.WHERE));
            for (Map<String, Object> user : result) {
                storage.deleteRow(user);
            }
            return result;
        }
    },
    SELECT {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            if (subQueries.containsKey(SubQuery.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Select. " +
                        "Параметр VALUES недопустим в данном контексте");
            if (!subQueries.containsKey(SubQuery.WHERE)) {                        //Запрос без where
                return storage.getAll();
            }
            return Parser.parseWhere(storage, subQueries.get(SubQuery.WHERE));
        }
    },
    UPDATE {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            List<Map<String, Object>> result;
            result = subQueries.containsKey(SubQuery.WHERE) ?
                    Parser.parseWhere(storage, subQueries.get(SubQuery.WHERE)) : storage.getAll();
            args = subQueries.get(SubQuery.VALUES);
            Map<String, Object> newValues = Parser.stringToMap(args);
            for (Map<String, Object> user : result) {
                newValues.entrySet().forEach((e) -> {
                    if (Objects.isNull(e.getValue())) user.remove(e.getKey());
                    else user.put(e.getKey(), e.getValue());
                });
                if (user.size() == 0 ) throw new IllegalArgumentException(
                        "В результате операции удаляются все значения пользователя!");
            }
            return result;
        }
    };
    private static final Map<String, Query> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    public static List<Map<String, Object>> processQuery(Storage storage, String request) {
        String[] splitReq = request.trim().split(" ");
        Optional<Query> optional = Query.fromString(splitReq[0]);
        if (optional.isEmpty()) throw new IllegalArgumentException("Неопознанная команда!");
        Query query = optional.get();
        request=request.substring(query.toString().length());
        return query.execute(storage, request);
    }

    public static Optional<Query> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    abstract List<Map<String, Object>> execute(Storage storage, String args);
}

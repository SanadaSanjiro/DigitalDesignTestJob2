package com.digdes.school;

import com.digdes.school.parser.Parser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            return null;
        }
    };

    public static List<Map<String, Object>> processQuery(Storage storage, String request) {
        String[] splitReq = request.trim().split(" ");
        Optional<Query> optional = Query.fromString(splitReq[0]);
        if (optional.isEmpty()) throw new IllegalArgumentException("Неопознанная команда!");
        Query query = optional.get();
        request=request.substring(query.toString().length());
        return query.execute(storage, request);
    }

    private static Optional<Query> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    private static final Map<String, Query> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    abstract List<Map<String, Object>> execute(Storage storage, String args);
}

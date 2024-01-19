package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Query implements Command {
    INSERT {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            if (!subQueries.containsKey(SubQuery.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                    "Отсутствует обязательный параметр VALUES");
            if (subQueries.containsKey(SubQuery.WHERE))
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                        "Параметр WHERE недопустим в данном контексте");
            args = subQueries.get(SubQuery.VALUES);
            List<Block> blocks = Parser.parseBlocks(args);
            if (blocks.isEmpty())
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                        "Данные не найдены.");
            User.UserBuilder userBuilder = new User.UserBuilder();
            for (Block block : blocks) {
                if (!block.getFilter().equals(LogicalFilter.EQUALS))
                    throw new IllegalArgumentException("Ошибка запроса Insert. " +
                            "Данные должны быть в формате 'поле' = значение");
                userBuilder.addColumn(block.getColumn(), block.getValue());
            }
            return storage.add(userBuilder.build());
        }
    },
    DELETE {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            if (subQueries.containsKey(SubQuery.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Delete. " +
                        "Параметр VALUES недопустим в данном контексте");
            if (!subQueries.containsKey(SubQuery.WHERE)) return storage.deleteAll();
            return null;
        }
    },
    SELECT {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            Map<SubQuery, String> subQueries = SubQuery.processSubQuery(args);
            if (subQueries.containsKey(SubQuery.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Select. " +
                        "Параметр VALUES недопустим в данном контексте");
            if (!subQueries.containsKey(SubQuery.WHERE)) {
                System.out.println("Запрос Select без Where");
                return storage.getAll();
            }
            return Processor.processLogicalList(storage.getAll(),
                    Parser.whereParser(subQueries.get(SubQuery.WHERE)));
        }
    },
    UPDATE {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    };

    public static List<Map<String, Object>> processQuery(Storage storage, String request) {
        String[] splitReq = request.trim().split(" ");
        Optional<Query> optional = Query.fromString(splitReq[0]);
        if (optional.isEmpty()) throw new IllegalArgumentException("Неопознанная команда!");
        Query query = optional.get();
        request=request.substring(query.toString().length());
        System.out.println("Выполняется команда: " + query + " " + request);
        return query.execute(storage, request);
    }

    public static Optional<Query> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    private static final Map<String, Query> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));
}

package com.digdes.school;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Query implements Command {
    INSERT {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            args = SubQuery.VALUES.process(args);
            List<Block> blocks = Parser.valuesParser(args);
            if (Objects.isNull(blocks))
                throw new IllegalArgumentException("Неверная строка параметров в запросе Insert!");
            User.UserBuilder userBuilder = new User.UserBuilder();
            for (Block block : blocks) {
                if (!block.getOperation().equals(LogicalFilter.EQUALS))
                    throw new IllegalArgumentException("Неверная строка параметров в запросе Insert!");
                userBuilder.addColumn(block.getColumn(), block.getValue());
            }
            return storage.addRow(userBuilder.build());
        }
    },
    DELETE {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    },
    SELECT {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    },
    UPDATE {
        @Override
        public List<Map<String, Object>> execute(Storage storage, String args) {
            return null;
        }
    };

    public static List<Map<String, Object>> parseQuery(Storage s, Query q, String request) {
        return q.execute(s, request);
    }

    public static Optional<Query> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    private static final Map<String, Query> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

}

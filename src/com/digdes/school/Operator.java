package com.digdes.school;

import java.util.EnumMap;
import java.util.Map;

/**
 * Перечисление определяет возможные операторы, а также задает метод их обработки.
 */
public enum Operator {
    /**
     * Используется при вставке новых или изменении существующих записей (команды INSERT, UPDATE), передавая значения
     * колонок для создания новой записи или для изменения существующих.
     * Параметры должны быть в формате: имя колонки = значение (через запятую).
     * Оператор от параметров отделяет пробел
     */
    VALUES {
        public String process (String request) {
            String[] args = request.trim().split(" ");
            if (!args[0].equalsIgnoreCase(this.toString())) //Проверка что строка начинается с VALUES
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует VALUES");
            return getArgsString(args);
        }
    },
    /**
     * Используется для фильтрации списка записей таблицы, к которому применяется команда,
     * по одному или нескольким условиям, разделенным логическими операторами.
     * Перечень допустимых условий определяется в перечислении RelationalOperator. Перечень допустимых
     * логических операций - в перечислении LogicalOperator.
     */
    WHERE {
        public String process(String request) {
            String[] args = request.trim().split(" ");
            if (!args[0].equalsIgnoreCase(this.toString())) //Проверка что строка начинается с WHERE
                throw new IllegalArgumentException("Неверный формат команды. Отсутствует WHERE");
            return getArgsString(args);
        }
    };

    /**
     * Метод превращает строку в отображение операторов и их параметров.
     * @param query строка запроса из операторов и их параметров
     * @return Map с операторами в качестве ключей и строками с соответствующими запросами в качестве значений
     */
    public static Map<Operator, String> processOperator(String query) {
        query=query.trim();
        Map<Operator, String> result = new EnumMap<>(Operator.class);

        // Сначала происходит поиск и обработка оператора WHERE
        int whereStart = query.toUpperCase().indexOf(WHERE.toString());
        if (!(whereStart==-1)) {                                            // если найден
            result.put(WHERE, WHERE.process(query.substring(whereStart)));  // добавляем WHERE и его аргументы
            query=query.substring(0, whereStart);                           // и удаляем их из строки запроса
        }

        // Затем обработка оператора VALUES, если он есть - он всегда будет в начале строки
        if (query.toUpperCase().startsWith(VALUES.toString())) {
            result.put(VALUES, VALUES.process(query));
        }
        return result;
    }

    /**
     * Пересобирает аргументы, убирая лишние пробелы.
     * @param s массив, состоящий из разбитых на части параметров
     * @return строка из параметров, разделенных строго одним пробелом
     */
    private static String getArgsString(String[] s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i<s.length; i++) {
            sb.append(s[i]).append(" ");
        }
        return sb.toString();
    }

    /**
     * Обрабатывает запрос, проверяя наличие в нем оператора.
     * @param request строка запроса, начинающаяся с оператора и содержащая один или несколько разделенных
     * запятыми или логическими операторами параметров
     * @return Возвращает обработанрную строку параметров без оператора
     */
    abstract String process (String request);
}

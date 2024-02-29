package com.digdes.school;

import com.digdes.school.parser.Parser;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Перечисление определяет допустимые команды, а также задает метод обработки каждой.
 */
public enum Query {
    /**
     * Команда вставки новой строки. Обязательно требуется наличие оператора VALUES с параметрами, иначе
     * выдается исключение. Поля параметров VALUES должны быть в формате "имя поля = значение", символы, отличные от =
     * вызывают исключение.
     * Оператор WHERE с данной командой недопустим.
     */
    INSERT {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<Operator, String> operators = Operator.processOperator(args); // Обработка операторов
            if (!operators.containsKey(Operator.VALUES))     // Проверка на  наличие оператора VALUES
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                    "Отсутствует обязательный параметр VALUES");
            if (operators.containsKey(Operator.WHERE))       // И отсутсвие оператора Where
                throw new IllegalArgumentException("Ошибка запроса Insert. " +
                        "Параметр WHERE недопустим в данном контексте");
            args = operators.get(Operator.VALUES);                 // Получает у оператора VALUES список параметров
            Map<String, Object> user = Parser.stringToMap(args);    // и передает его в парсер для преобразования в Map

            // Проверка на отсутсвие параметров со значением NULL (недопустимо при создании новой записи,
            // поля, не содержащие данных, просто не указываются в запросе)
            for (Object value : user.values()) {
                if (Objects.isNull(value))
                    throw new IllegalArgumentException(
                            "Значение поля при добавлении пользователя не может быть null!");
            }
            return storage.add(user);                          // Помещает пользователя в хранилище
        }
    },

    /**
     * Команда удаления строки. Может использоваться с оператором WHERE с параметрами.
     * При отсутсвии такового удаляет все данные в хранилище
     * Оператор VALUES с данной командой недопустим.
     */
    DELETE {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<Operator, String> operators = Operator.processOperator(args); // Обработка операторов
            if (operators.containsKey(Operator.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Delete. " +
                        "Параметр VALUES недопустим в данном контексте");
            if (!operators.containsKey(Operator.WHERE)) return storage.deleteAll();
            List<Map<String, Object>> filtered = Parser.parseWhere(storage, operators.get(Operator.WHERE));
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> user : filtered) {
                result.addAll(storage.deleteRow(user));
            }
            return result;
        }
    },

    /**
     * Команда выбора строк. Может использоваться с оператором WHERE с параметрами.
     * При отсутсвии такового возвращает все данные, находящиеся в хранилище.
     * Оператор VALUES с данной командой недопустим.
     */
    SELECT {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<Operator, String> operators = Operator.processOperator(args); // Обработка операторов
            if (operators.containsKey(Operator.VALUES))
                throw new IllegalArgumentException("Ошибка запроса Select. " +
                        "Параметр VALUES недопустим в данном контексте");
            if (!operators.containsKey(Operator.WHERE)) {                        //Запрос без where
                return storage.getAll();
            }
            return Parser.parseWhere(storage, operators.get(Operator.WHERE));
        }
    },

    /**
     * Команда изменения строк. Обязательно требуется наличие оператора VALUES с параметрами, иначе
     * выдается исключение. Может использоваться с оператором WHERE.
     */
    UPDATE {
        @Override
        List<Map<String, Object>> execute(Storage storage, String args) {
            Map<Operator, String> operators = Operator.processOperator(args); // Обработка операторов
            if (!operators.containsKey(Operator.VALUES))     // Проверка на  наличие оператора VALUES
                throw new IllegalArgumentException("Ошибка запроса Update. " +
                        "Отсутствует обязательный параметр VALUES");
            List<Map<String, Object>> result;
            result = operators.containsKey(Operator.WHERE) ?   // Список, отфильтрованный по условиям WHERE
                    Parser.parseWhere(storage, operators.get(Operator.WHERE)) : storage.getAll(); // либо полный список
            args = operators.get(Operator.VALUES);             // Получает у оператора VALUES список параметров
            Map<String, Object> newValues = Parser.stringToMap(args); // и передает его в парсер

            // Обработка пользователей, попавших в список
            for (Map<String, Object> user : result) {
                newValues.forEach((key, value) -> {
                    if (Objects.isNull(value)) user.remove(key); // если стобец null - он удаляется
                    else user.put(key, value);                   // иначе в столбец вносится новое значение
                });

                // Если в результате операции все столбцы у пользователя удаляются
                // генерируется исключение, поскольку это недопустимо по условиям задачи
                if (user.size() == 0 ) throw new IllegalArgumentException(
                        "В результате операции удаляются все значения пользователя!");
            }
            return result;
        }
    };

    // Используется для преобразования строк в значения перечисления
    private static final Map<String, Query> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    /**
     * Метод обработки команд. Принимает хранилище и строковый запрос, возвращает список пользователей
     * При некорректном запросе генерирует IllegalArgumentException.
     * @param storage Хранилище данных
     * @param request Метод принимает строку в формате КОМАНДА (ОПЕРАТОР1 параметр1, параметр2... параметрN)
     * Строка команды. Команды и параметры могут быть как в верхнем, так и в нижнем регистре,
     * лишние пробелы игнорируются. Наименование колонок и строковые значения оборачиваются в одинарные
     * кавычки, а числа и булевые значения без них
     * Также, в запросах INSERT допустимо значение NULL без кавычек.
     * @return Возвращает таблицу в виде List<Map<String,Object>> . Где List это список строк в таблице.
     * Map это значения в колонках, где ключом Map является наименование колонки в виде строки, а значением
     * Map является значение в ячейке таблицы (допустимые типы значений в ячейках: Long, Double, Boolean, String)
     */
    public static List<Map<String, Object>> processQuery(Storage storage, String request) {
        String[] splitReq = request.trim().split(" ");
        Optional<Query> optional = Query.fromString(splitReq[0]);
        if (optional.isEmpty()) throw new IllegalArgumentException("Неопознанная команда!");
        Query query = optional.get();
        request=request.substring(query.toString().length());
        return query.execute(storage, request);
    }

    /**
     * Преобразует строку в элемент перечисления.
     * @param s Строка, соответствующая одному из элекментов перечисления. Регистр не важен
     * @return Элемент перечисления
     */
    public static Optional<Query> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase()));
    }

    /**
     * Абстрактный метод, определяющий то, как команда обрабатывает запрос.
     * @param storage Хранилище данных
     * @param args Метод должен принимать строку в формате КОМАНДА (ОПЕРАТОР1 параметр1, параметр2... параметрN)
     * Строка команды. Команды и параметры могут быть как в верхнем, так и в нижнем регистре,
     * лишние пробелы игнорируются. Наименование колонок и строковые значения оборачиваются в одинарные
     * кавычки, а числа и булевые значения без них
     * @return Возвращает таблицу в виде List<Map<String,Object>> . Где List это список строк в таблице.
     * Map это значения в колонках, где ключом Map является наименование колонки в виде строки, а значением
     * Map является значение в ячейке таблицы (допустимые типы значений в ячейках: Long, Double, Boolean, String)
     */
    abstract List<Map<String, Object>> execute(Storage storage, String args);
}

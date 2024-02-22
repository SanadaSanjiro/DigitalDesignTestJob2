package com.digdes.school.parser;

import com.digdes.school.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс, предоставляющий статические методы обработки и преобразования строк
 */
public class Parser {
    // Шаблон, описывающий допустимые варианты значений (строковые значения оборачиваются в одинарные кавычки.
    // А числа и булевые значения без кавычек.
    // Регистр символов значения не имеет
    private static final String VALUES_PATTERN = "(?i)'?((\\d+\\.?\\d*)|('[%\\wа-яА-ЯёЁ-]+')|" +
            "(FALSE)|(TRUE)|(NULL))'?";

    // Шаблон, описывающий допустимую структуру блока. Составляется из шаблонов отдельных частей:
    // Имя столбца в одинарных кавычках;
    // Оператор отношения;
    // Значение.
    // Все это разделяется произвольным числом пробелов
    private static final String FULL_PATTERN = " *'("
            + getEnumPattern(Column.class) + "*)' *("
            + getEnumPattern(RelationalOperator.class) + "*) *"
            + VALUES_PATTERN + " *";
    private static final Pattern BLOCK_PATTERN = Pattern.compile(FULL_PATTERN);

    /**
     * Статический метод, преобразующий строку символов в запись таблицы
     * @param string строка из разделенных запятыми значений полей записи в формате
     * 'имя столбца' ОПЕРАТОР_ОТНОШЕНИЯ значение. Наименование колонки должно быть в одинарных кавычках.
     * Строковые значения также оборачиваются в одинарные кавычки. А числа, булевые значения и NULL без кавычек.
     * Пример корректной строки: 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true
     * Регистр символов значения не имеет
     * @return в случае успешного преобразования возвращает запись таблицы (Объект Map)
     */
    public static Map<String, Object> stringToMap(String string) {
        String[] blocks = string.split(",");
        List<Block> values = new ArrayList<>();
        for (String s : blocks) {
            values.add(getBlock(s));
        }
        if (values.isEmpty())
            throw new IllegalArgumentException("Ошибка вставки. " +
                    "Данные не найдены.");
        User.UserBuilder userBuilder = new User.UserBuilder();      // Создается промежуточный объект User
        for (Block block : values) {
            if (!block.getRelation().equals(RelationalOperator.EQUALS))
                throw new IllegalArgumentException("Ошибка вставки. " +
                        "Данные должны быть в формате 'поле' = значение");
            userBuilder.addColumn(block.getColumn(), block.getValue());
        }
        return userToMap(userBuilder.build());                      // Который преаобразуется в запись Map
    }

    /**
     * Статический метод, фильтрующий список записей таблицы по условиям из строки условий оператора WHERE
     * @param storage Хранилище записей
     * @param string строка условий, разделенных логическими операторами и произвольным количеством пробелов.
     * Регистр символов значения не имеет.
     * Пример корректной строки: 'age'>=30 and 'lastName' ilike '%п%'
     * @return возвращает лист, записи в котором удовлетворяют всем условиям, либо пустой лист, если таких не найдено
     */
    public static List<Map<String, Object>> parseWhere(Storage storage, String string) {
        List<Condition> conditions = new ArrayList<>();               //список условий (блок + логический оператор)
        String logicalRegexp = " ?" + getEnumPattern(LogicalOperator.class) + "+ ?"; // шаблон логического оператора
        String substring = "";
        Pattern logicalPattern = Pattern.compile(logicalRegexp);
        Matcher logicalMatcher;
        Matcher blockMatcher = BLOCK_PATTERN.matcher(string);
        Optional<LogicalOperator> optional;
        Block block;

        while (blockMatcher.find()) {                           // пока находятся блоки с условиями
            substring = getSubstring(blockMatcher, string);
            block = getBlock(substring); // Преобразуем подстроку в Block (столбец, оператор отношения, значение)
            string = string.replaceFirst(substring, ""); // удаляем обработанную подстроку
            logicalMatcher = logicalPattern.matcher(string);       // ищем в строке логический оператор
            if (logicalMatcher.find()) {                           // если находим - добавляем в цепочку
                substring = getSubstring(logicalMatcher, string);
                optional = LogicalOperator.fromString(substring);
                if (!optional.isPresent()) throw new IllegalArgumentException("Ошибка в обработке условий");
                conditions.add(new Condition(optional.get(), block));
                string = string.replaceFirst(substring, "");
            }
            else {                                                // если логический оператор не найден
                conditions.add(new Condition(null, block));   // значит достигнут конец цепочки условий
                break;   // добавляется последний элемент без логического оператора и цикл прерывается
                }
            blockMatcher = BLOCK_PATTERN.matcher(string);
        }
        return getFilteredList(storage, conditions); // возвращаем отфильтрованный список записей
    }

    /**
     * Метод, возвращающий подстроку
     * @param matcher объект matcher, выполняющий поиск по строке
     * @param string строка символов, в которой проводится поиск
     * @return возвращает искомую подстроку
     */
    private static String getSubstring(Matcher matcher, String string) {
        int start = matcher.start();
        int end = matcher.end();
        return string.substring(start, end);
    }

    /**
     * Статический метод, преобразующий строку вида 'Стобец' отношение значение в промежуточный объект Block для последующей
     * обработки данных (создания новой записи таблицы или изменение существующих)
     * @param string строка вида Имя столбца Отношение Значение, разделенных произвольным количеством пробелов.
     * Примеры корректных строк: 'id'=3; 'lastName' ilike '%п%'; 'age' > 4
     * @return Объект Block
     */
    private static Block getBlock(String string) {
        Matcher matcher = BLOCK_PATTERN.matcher(string);
        boolean isMatches = matcher.matches();
        Block block;
        if (isMatches) {
            String column = matcher.group(1);
            String operation = matcher.group(2);
            String value = matcher.group(3);
            Column c = getColumn(column);
            RelationalOperator op = getOperation(operation);
            Object val = value.equalsIgnoreCase("NULL") ? null : stringToValue(c, value);
            block = new Block(c, op, val);
        } else throw new IllegalArgumentException("Ошибка обработки блока команды: " + string);
        return block;
    }

    /**
     * Статический метод, преобразующий строку в соответствующий ей тип столбца.
     * Если соответсвие не найдено генерируется исключение IllegalArgumentException
     * @param string Строка с наименованием стобца
     * @return Экземпляр перечисления Column
     */
    private static Column getColumn(String string) {
        Optional<Column> result = Column.fromString(string);
        if (result.isPresent()) return result.get();
        throw new IllegalArgumentException("Неверный тип колонки " + string);
    }

    /**
     * Статический метод, преобразующий строку в соответствующий ей тип оператора отношения
     * (больше, меньше, равно и т.п.). Если соответсвие не найдено генерируется исключение IllegalArgumentException
     * @param string Строка с симвоалми, соответствующими одному из операторов отношения
     * @return Экземпляр перечисления RelationalOperator
     */
    private static RelationalOperator getOperation(String string) {
        Optional<RelationalOperator> result = RelationalOperator.fromString(string.toUpperCase());
        if (result.isPresent()) return result.get();
        throw new IllegalArgumentException("Неверный тип операции " + string);
    }

    // Преобразует строку в объект соответствуюшего столбцу типа

    /**
     * Статический метод, преобразующий строку в объект, тип которого соответствует типу данных столбца
     * @param column Экезмпляр перечисления Column
     * @param string Строка, содержащая значение, которое требуется привести к типу столбца
     * @return Объект, тип которого соответствует типу данных, указанных в Column
     */
    private static Object stringToValue(Column column, String string) {
        return column.apply(string);
    }

    // Возвращают регулярное выражение со значениями наименований перечисления
    // для использования в качестве паттерна при обработке строк

    /**
     * Статический метод, который генерирует регулярное выражение со значениями строкового представления
     *  элементов произвольного перечисления для использования в качестве шаблона при обработке строк
     * @param c Класс, расширяющий Enum
     * @return строка
     * @param <T> Класс, расширяющий Enum
     */
    private static <T extends Enum<T>> String getEnumPattern (Class<T> c) {
        String result = Arrays.stream(c.getEnumConstants())
                .map(Enum::toString)
                .collect(Collectors.joining("|"));
        return "(?i)" + result; // (?i) - флаг "не учитывать регистр при поиске"
    }

    /**
     * Статический метод, предоставляющий полученный из хранилища список записей, отфильтрованный по списку условий
     * @param storage Хранилище.
     * @param conditions Список условий (блок и логический оператор, последняя запись должна быть без лог. опера-ра)
     * @return Список записей, либо пустой список, если не найдено ни одной соответствующей условиям записи
     */
    private static List<Map<String, Object>> getFilteredList(Storage storage, List<Condition> conditions) {
        List<Map<String, Object>> list = storage.getAll();
        return LogicalOperator.applyConditions(list, conditions);
    }

    // Преобразует объект User в map для помещения в хранилище

    /**
     * Статический метод, преобразующий промежуточаный объект User в запись таблицы для помещения в хранилище
     * @param user Объект User
     * @return Возвращает Map<String, Object>, представляющий собой запись таблицы
     */
    private static Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        user.getMap().forEach((key1, value) -> {
            var key = key1.toString();
            map.put(key, value);
        });
        return map;
    }
}

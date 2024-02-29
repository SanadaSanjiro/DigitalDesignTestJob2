package com.digdes.school.parser;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


/**
 * Перечисление, описывающее используемые локические операции, используемые при фильтрации записей в операторе WHERE
 * Также, в перечилении задаются методы обработки условий каждым оператором.
 * Помимо этого, перечисление предоставляет статический метод для проверки соотвутсвия списка записей таблицы цепочке
 * разделенных логическими операторами условий
 */
enum LogicalOperator {
    AND {
        @Override
        public boolean apply(boolean cond1, boolean cond2) {
            return cond1 && cond2;
        }
    },
    OR {
        @Override
        public boolean apply(boolean cond1, boolean cond2) {
            return cond1 || cond2;
        }
    };
    private static final Map<String, LogicalOperator> stringToEnum = Stream.of(values()).collect(
            toMap(Object::toString, e->e));

    static Optional<LogicalOperator> fromString(String s) {
        return Optional.ofNullable(stringToEnum.get(s.toUpperCase().trim()));
    }

    /**
     * Определяет метод, описывающий применение логического оператора к двум условиям.
     * @param cond1 сюда пеередается результат проверки соответствия данных столбца условию
     * @param cond2 сюда пеередается результат проверки следующих столбца и условия
     * @return возвращает результат применения логического оператора к полученным условиям
     */
    public abstract boolean apply(boolean cond1, boolean cond2);

    /**
     * Метод для проверки соотвутсвия списка записей таблицы цепочке разделенных логическими операторами условий.
     * @param userList список записей таблицы, которые будут проверятся на соответствие цепочке условий
     * @param condList цепочка условий (блок + логический оператор), в последнем условии
     * логический оператор должен быть null
     * @return возвращает лист, записи в котором удовлетворяют всем условиям, либо пустой лист, если таких не найдено
     */
    public static List<Map<String, Object>> applyConditions(List<Map<String, Object>> userList,
                                                     List<Condition> condList) {
        return userList.stream()
                .filter(user -> LogicalOperator.processChain(condList, user))
                .collect(Collectors.toList());
    }

    /**
     * Метод, проверяющий одну запись таблицы на соответствие цепочке условий.
     * @param list цепочка условий (блок + логический оператор), в последнем условии
     * логический оператор должен быть null
     * @param user Объект Map, удовлетворяющий требованиям, предъявляемым к записям таблицы.
     * @return TRUE, если запись удовлетворяет условиям
     */
    private static boolean processChain( List<Condition> list, Map<String, Object> user) {
        if (list.isEmpty()) throw new IllegalArgumentException("Пустой список параметров!");
        Iterator<Condition> iterator = list.iterator();
        Condition condition = iterator.next();
        LogicalOperator lo;
        Block block = condition.getBlock();
        boolean isTrue = checkCondition(block, user);

        // проверяем цепочку условий до тех пор, пока либо результат вычислений не будет однозначно TRUE,
        // либо до конца цепочки (отсутствуют следующие элементы и логический оператор в последнем элементе NULL)
        while (true) {
            lo = condition.getLogical();
            // если за условием отсутсвует логический оператор - возвращается результат проверки условия
            if (Objects.isNull(lo)) return isTrue;
            // если предыдущее условие не выполнено (логический оператор имеется), но следующий элемент цепочки
            // отсутсвует, значи условия фильтрации заданы неверно
            if (!iterator.hasNext()) throw new IllegalArgumentException("Пропущено условие в логическом операторе!");
            // Если результат проверки true и за ним следует логическое ИЛИ нет смысла в дальнейшей проверке,
            // возвращается TRUE
            if (isTrue&&lo.equals(LogicalOperator.OR)) return true;
            // иначе применяем логический оператор к результату предыдущих вычислений и результату проверки
            // следующего условия из цепочки
            condition = iterator.next();
            block = condition.getBlock();
            isTrue = lo.apply(isTrue, checkCondition(block, user));
        }
    }

    /**
     * Метод для проверки соответствия записи таблицы одному условию.
     * @param block блок, состоящий из столбца, оператора отношения, и сравниваемого значения
     * @param user Объект Map, удовлетворяющий требованиям, предъявляемым к записям таблицы.
     * @return TRUE, если запись удовлетворяет условию
     * @param <T> Тип данных столбца таблицы, должен реализовывать интерфейс Comparable
     */
    private static <T extends Comparable<T>>
    boolean checkCondition(Block block, Map<String, Object> user) {
        Column column = block.getColumn();
        T condition = Column.castValue(column, block.getValue());
        RelationalOperator relation = block.getRelation();
        T value = Column.castValue(column, user.get(column.toString()));
        return relation.check(condition, value);
    }
}

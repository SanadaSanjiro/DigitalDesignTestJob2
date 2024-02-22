package com.digdes.school.parser;

/**
 * Промежуточный класс, используемый для обработки данных столбцов таблицы при создании новых записей и фильтрации
 * существующих.
 */
class Block {
    private final Column column;                        // Столбец таблицы
    private final Object value;                         // Значение столбца
    private final RelationalOperator relation;          // Оператор отношения (больше, равно, похоже и т.п.)
    Block(Column c, RelationalOperator op, Object obj) {
        column = c;
        value = obj;
        relation = op;
    }

    Column getColumn() { return column; }
    Object getValue() {
        return value;
    }
    RelationalOperator getRelation() {
        return relation;
    }
    @Override
    public String toString() {
        return "Block{" +
                "column: " + column +
                ", filter: " + relation +
                ", value: " + value +
                '}';
    }
}

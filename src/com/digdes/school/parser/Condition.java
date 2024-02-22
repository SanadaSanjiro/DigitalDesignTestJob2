package com.digdes.school.parser;


/**
 * Промежуточный класс, используемый при фильтрации записей. Объект состоит из блока условия, применяемого к столбцу и
 * логического оператора (может быть null)
 */
class Condition {
    private final LogicalOperator logical;
    private final Block block;
    Condition(LogicalOperator lo, Block b) {
        logical = lo;
        block = b;
    }

    LogicalOperator getLogical() {
        return logical;
    }

    Block getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "logical=" + logical +
                ", block=" + block +
                '}';
    }
}

package com.digdes.school.parser;

class Block {
    private final Column column;
    private final Object value;
    private final RelationalOperator relation;
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

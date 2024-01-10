package com.digdes.school;

public class Block {
    private final Column column;
    private final Object value;
    private final Operation operation;
    public Block(Column c, Operation op, Object obj) {
        column = c;
        value = obj;
        operation = op;
    }

    public Column getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public Operation getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return "Block{" +
                "column: " + column +
                ", operation: " + operation +
                ", value: " + value +
                '}';
    }
}

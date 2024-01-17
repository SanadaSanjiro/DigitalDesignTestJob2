package com.digdes.school;

public class Block {
    private final Column column;
    private final Object value;
    private final LogicalFilter filter;
    public Block(Column c, LogicalFilter op, Object obj) {
        column = c;
        value = obj;
        filter = op;
    }

    public Column getColumn() { return column; }
    public Object getValue() {
        return value;
    }
    public LogicalFilter getFilter() {
        return filter;
    }
    @Override
    public String toString() {
        return "Block{" +
                "column: " + column +
                ", filter: " + filter +
                ", value: " + value +
                '}';
    }
}

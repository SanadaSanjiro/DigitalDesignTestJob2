package com.digdes.school;

public class Block {
    private final Column column;
    private final Object value;
    private final Operation operation;
    private final String pattern = " *[‘'`][a-z]+[’'`] *";
    public Block(Column c, Operation op, Object obj) {
        column = c;
        value = obj;
        operation = op;
    }
}

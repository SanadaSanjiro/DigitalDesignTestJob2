package com.digdes.school;

public class Condition {
    private final LogicalOperator logical;
    private final Block block;
    public Condition(LogicalOperator lo, Block b) {
        logical = lo;
        block = b;
    }

    public LogicalOperator getLogical() {
        return logical;
    }

    public Block getBlock() {
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

package com.digdes.school.parser;

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

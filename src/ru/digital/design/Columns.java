package ru.digital.design;

public enum Columns {
    ID("id"),
    LASTNAME("lastName"),
    AGE("age"),
    COST("cost"),
    ACTIVE("active");

    private final String columnName;
    Columns(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}

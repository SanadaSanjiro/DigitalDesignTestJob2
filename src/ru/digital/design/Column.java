package ru.digital.design;

public enum Column {
    ID("id", Long.class),
    LASTNAME("lastName", String.class),
    AGE("age", Long.class),
    COST("cost", Double.class),
    ACTIVE("active", Boolean.class);

    private final String columnName;
    private final Class columnClass;
    Column(String columnName, Class columnClass) {
        this.columnName = columnName;
        this.columnClass = columnClass;
    }

    public String getColumnName() {
        return columnName;
    }

    public Class getColumnClass() {
        return this.columnClass;
    }
}

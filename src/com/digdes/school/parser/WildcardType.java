package com.digdes.school.parser;

//Класс содержит типы регулярных выражений для обработки подстановочных знаков в строковых фильтрах

/**
 * Перечисление, содержащее типы подстановочных символов, используемых при обработке строковых значений операторами
 * Like и iLike, а также предоставляющее метод для сравнения строк символов с учетом подстановочных символов
 */
enum WildcardType {
    BOTH("%(.*)%", ".*", ".*"),     // произвольные символы с обеих сторон строки
    LEFT("%(.*[^%])", ".*", ""),    // произвольные символы в начале строки
    RIGHT("([^%].*)%", "", ".*"),   // произвольные символы в конце строки
    NONE("([^%].*[^%])", "", "");   // строка должна в точности соответствовать значению

    private final String REGEXP, PREFIX, SUFFIX;
    WildcardType (String regexp, String prefix, String suffix) {
        REGEXP = regexp;
        PREFIX = prefix;
        SUFFIX = suffix;
    }
    //Метод возвращает enum, соответствующий строке запроса

    /**
     * Метод возвращает экземпляр перечисления, соответствующий строке запроса
     * @param condition Строковое представление перечисления. Может быть как в верхнем, так и в нижнем регистре.
     * Символом подставновки служит знак %, обозначающий любое количество любых символов на его месте, который
     * может находиться в начале, в конце строки, в начале и в конце строки или может отсутсвовать.
     * Примеры использования символа подставновки: 'строка', 'строка%', '%строка%', '%строка'
     * Соответствующие этим примерам экземпляры перечисления: NONE, RIGHT, BOTH, LEFT
     * @return Возвращает объект перечисления (тип подстановки)
     */
    static WildcardType getWildcardType(String condition) {
        condition = condition.replaceAll("'", "");
        for(WildcardType wt : WildcardType.values()) {
            if (condition.matches(wt.REGEXP)) return wt;
        }
        throw new IllegalArgumentException("Неверный формат запроса строкового параметра");
    }

    private String getPrefix() {
        return PREFIX;
    }

    private String getSuffix() {
        return SUFFIX;
    }

    /**
     * Метод для сравнения строк символов с учетом подстановочных символов
     * @param wc Тип используемых при сравнении символов подстановки (любые сисволы в начале строки, в конце строки,
     *           с обеих сторон строки или точное соответствие строк)
     * @param condition Условие, с которым производится сравнение
     * @param value Сравниваемая строка символов
     * @return TRUE, если строка соответсвует условию с учетом символов подстановки
     */
    static boolean matches(WildcardType wc, String condition, String value) {
        condition = condition.replaceAll("%", "").replaceAll("'", "");
        value = value.replaceAll("'", "");
        String rx = wc.getPrefix() + condition + wc.getSuffix();
        return value.matches(rx);
    }
}

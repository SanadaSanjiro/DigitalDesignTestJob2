package com.digdes.school;

import java.util.List;
import java.util.Map;

/**
 * Класс, через метод execute которого происходит обработка команд соглсно условиям задачи.
 * Имеет единственный конструктор без аргументов и единственный метод, принимающий строку
 * и возвращающий список объектов Map, содержащих информацию о пользователях.
 */
public class JavaSchoolStarter {
    private final Storage storage;
    public JavaSchoolStarter() {
        storage = new Storage();
    }

    /**
     * Метод принимает строку в формате КОМАНДА (ОПЕРАТОР1 параметр1, параметр2... параметрN)
     * (ОПЕРАТОР2 параметр1, параметр2... параметрM).
     * Данные сохраняются на время исполнения программы, последовательно можно вызвать несколько команд.
     * Исключение генерируется при получении команды, содержащей ошибки (неопознанная команда,
     * неверный формат параметров, и т.п.)
     * @param request Строка команды. Команды и параметры могут быть как в верхнем, так и в нижнем регистре,
     * лишние пробелы игнорируются. Наименование колонок и строковые значения оборачиваются в одинарные
     * кавычки, а числа и булевые значения без них
     * Также, в запросах INSERT допустимо значение NULL без кавычек
     * @return Возвращает таблицу в виде List<Map<String,Object>> . Где List это список строк в таблице.
     * Map это значения в колонках, где ключом Map является наименование колонки в виде строки, а значением
     * Map является значение в ячейке таблицы (допустимые типы значений в ячейках: Long, Double, Boolean, String)
     */
    public List<Map<String,Object>> execute(String request) {
        return Query.processQuery(storage, request);
    }
}

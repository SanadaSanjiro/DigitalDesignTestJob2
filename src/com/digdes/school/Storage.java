package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Класс обеспечивает хранение таблицы, а также предоставляет основные операции (CRUD) с ней.
 * Данные хранятся в виде List<Map<String,Object>>, где List это список строк в таблице. Map это значения в колонках,
 * где ключом Map является наименование колонки в виде строки, а значением Map является значение в ячейке
 * таблицы (допустимые типы значений в ячейках: Long, Double, Boolean, String).
 */
public class Storage {
    private final List<Map<String, Object>> list = new ArrayList<>();

    /**
     * Метод добавляет новую строку в таблицу.
     * @param user принимает Map<String, Object> и сохраняет ее в таблице. Корректность данных не проверяется!
     * @return возвращает список с добавленной записью в формате List<Map<String, Object>>
     */
    public List<Map<String, Object>> add(Map<String, Object> user) {
        List<Map<String, Object>> result = new ArrayList<>();
        list.add(user);
        result.add(user);
        return result;
    }

    /**
     * Метод удаляет одну строку из таблицы.
     * @param map принимает Map<String, Object>
     * @return возвращает список с удаленной записью в формате List<Map<String, Object>>. Если запись не найдена,
     * возвращает пустой список
     */
    public List<Map<String, Object>> deleteRow (Map<String, Object> map) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (!list.contains(map)) return result;
        result.add(map);
        list.remove(map);
        return result;
    }
    /**
     * Метод полностью очищает таблицу.
     * @return возвращает список со всеми запиями, хранившимися в таблице
     */
    public List<Map<String, Object>> deleteAll() {
        List<Map<String, Object>> result = new ArrayList<>(list);
        list.clear();
        return result;
    }

    /**
     * Метод возвращает список со всеми записями таблицы.
     * @return список со всеми записями в формате List<Map<String, Object>>
     */
    public List<Map<String, Object>> getAll() {
        return list;
    }


}

package com.test.inventorysockswarehouse.data.entity.editable_interface;

public interface EditableUpdate<ENTITY> {

    /**
     * Метод обновляет текущую Сущность исходя из данных текущей Сущности и обновлённой Сущности
     * Из текущей Сущности должны перезаписываться системные поля, id, createdAt
     *
     * @param updated Обновленная информация Сущности
     * @return Обновленная Сущность
     */
    ENTITY update(ENTITY updated);

}

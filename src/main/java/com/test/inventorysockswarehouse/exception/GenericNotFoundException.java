package com.test.inventorysockswarehouse.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericNotFoundException extends NullPointerException {

    /**
     * Общее для всех "Не найден" исключение
     *
     * @param id    Идентификатор искомой сущности
     * @param clazz Искомый класс
     */
    public GenericNotFoundException(Long id, Class<?> clazz) {
        super(String.format("Сущность %s с идентификатором %s не найдена", clazz.getCanonicalName(), id));

        log.error(String.format("Сущность %s с идентификатором %s не найдена", clazz.getCanonicalName(), id));
    }

    /**
     * "Не найден" исключение
     *
     * @param color Цвет
     * @param percentageCotton Процент содержания хлопка
     * @param clazz Искомый класс
     */
    public GenericNotFoundException(String color, Integer percentageCotton, Class<?> clazz) {
        super(String.format("Сущность %s с цветом %s и процентным содержанием хлопка %s не найдена",
                clazz.getCanonicalName(), color, percentageCotton));

        log.error(String.format("Сущность %s с цветом %s и процентным содержанием хлопка %s не найдена",
                clazz.getCanonicalName(), color, percentageCotton));
    }
}

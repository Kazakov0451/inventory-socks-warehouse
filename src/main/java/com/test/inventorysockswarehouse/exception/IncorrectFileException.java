package com.test.inventorysockswarehouse.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class IncorrectFileException extends IOException {

    /**
     * Исключение некорректный файл или произошла какая та ошибка
     * @param message Информация об исключении
     */
    public IncorrectFileException(String message) {
        super(message);

        log.error(message);
    }
}

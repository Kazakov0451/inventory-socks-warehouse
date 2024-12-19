package com.test.inventorysockswarehouse.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsufficientQuantityInWarehouseException extends RuntimeException {

    /**
     * Исключение недостаточно кол-во на складе
     * @param message Информация об исключении
     */
    public InsufficientQuantityInWarehouseException(String message) {
        super(message);

        log.error(message);
    }
}

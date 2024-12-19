package com.test.inventorysockswarehouse.controller;

import com.test.inventorysockswarehouse.exception.GenericNotFoundException;
import com.test.inventorysockswarehouse.exception.IncorrectFileException;
import com.test.inventorysockswarehouse.exception.InsufficientQuantityInWarehouseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    /**
     * Возвращает текс ошибки некорректный файл и статус код
     *
     * @param e класс {@link IncorrectFileException}
     * @return Тело ошибки
     */
    @ExceptionHandler(IncorrectFileException.class)
    public ResponseEntity handleIncorrectFileException(IncorrectFileException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Возвращает текс ошибки недостаточно носков на складе и статус код
     *
     * @param e класс {@link InsufficientQuantityInWarehouseException}
     * @return Тело ошибки
     */
    @ExceptionHandler(InsufficientQuantityInWarehouseException.class)
    public ResponseEntity handleInsufficientQuantityInWarehouseException(InsufficientQuantityInWarehouseException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Возвращает текс ошибки не найдена сущность на складе и статус код
     *
     * @param e класс {@link GenericNotFoundException}
     * @return Тело ошибки
     */
    @ExceptionHandler(GenericNotFoundException.class)
    public ResponseEntity handleGenericNotFoundException(GenericNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

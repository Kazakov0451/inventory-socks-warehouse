package com.test.inventorysockswarehouse.model.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OperationForPercentageCotton {

    /**
     * Больше, чем
     */
    moreThan("Больше, чем"),

    /**
     * Меньше, чем
     */
    lessThan("Меньше, чем"),

    /**
     * Равен
     */
    equal("Равен");

    private final String stringValue;
}

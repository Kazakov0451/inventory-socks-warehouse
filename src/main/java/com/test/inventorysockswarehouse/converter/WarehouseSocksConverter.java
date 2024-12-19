package com.test.inventorysockswarehouse.converter;

import com.test.inventorysockswarehouse.data.entity.WarehouseSocks;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksRequestDto;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WarehouseSocksConverter {

    /**
     * Конвертирует {@link WarehouseSocksRequestDto} в {@link WarehouseSocks}
     *
     * @param warehouseSocksDto ДТО объект
     * @return ДАО объект
     */
    public WarehouseSocks toEntity(WarehouseSocksRequestDto warehouseSocksDto) {
        if (warehouseSocksDto == null) {
            return null;
        }

        return WarehouseSocks.builder()
                .color(warehouseSocksDto.getColor())
                .percentageCotton(warehouseSocksDto.getPercentageCotton())
                .quantity(warehouseSocksDto.getQuantity())
                .build();
    }

    /**
     * Конвертирует {@link WarehouseSocks} в {@link WarehouseSocksResponseDto}
     *
     * @param warehouseSocks ДАО объект
     * @return ДТО объект
     */
    public WarehouseSocksResponseDto toDto(WarehouseSocks warehouseSocks) {
        if (warehouseSocks == null) {
            throw new IllegalArgumentException("Недопустимо передавать null значение");
        }

        return WarehouseSocksResponseDto.builder()
                .id(warehouseSocks.getId())
                .color(warehouseSocks.getColor())
                .percentageCotton(warehouseSocks.getPercentageCotton())
                .quantity(warehouseSocks.getQuantity())
                .build();
    }
}

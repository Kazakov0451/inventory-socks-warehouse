package com.test.inventorysockswarehouse.service.warehouse_socks;

import com.test.inventorysockswarehouse.model.dto.enums.OperationForPercentageCotton;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksRequestDto;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface WarehouseSocksService {

    /**
     * Регистрация прихода носков
     *
     * @param warehouseSocksDto ДТО для приходов носков
     * @return ДТО Остаток на складе
     */
    WarehouseSocksResponseDto income(WarehouseSocksRequestDto warehouseSocksDto);

    /**
     * Регистрация отпуска носков
     *
     * @param warehouseSocksDto ДТО для отпуска носков
     * @return ДТО Остаток на складе
     */
    WarehouseSocksResponseDto outcome(WarehouseSocksRequestDto warehouseSocksDto);

    /**
     * Получения общего кол-во носков на складе, используя фильтрацию
     *
     * @return Кол-во носков
     */
    Integer getQuantitySocksOnWarehouseByFilter(String color, Integer percentageCotton,
                                                OperationForPercentageCotton operationForPercentageCotton,
                                                String rangePercentageCotton);

    /**
     * Редактирования данных носков на складе
     *
     * @param warehouseSocksDto ДТО для редактирования носков на складе
     * @return ДТО носков на складе
     */
    WarehouseSocksResponseDto update(Long warehouseSocksId, WarehouseSocksRequestDto warehouseSocksDto);

    /**
     * Загрузка партий носков на склад из Excel
     *
     * @param file Файл Excel
     * @return ДТО носков на складе
     */
    List<WarehouseSocksResponseDto> incomeByExcel(MultipartFile file) throws IOException;
}

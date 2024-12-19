package com.test.inventorysockswarehouse.controller;

import com.test.inventorysockswarehouse.model.dto.enums.OperationForPercentageCotton;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksRequestDto;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksResponseDto;
import com.test.inventorysockswarehouse.service.warehouse_socks.WarehouseSocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController("api/v1/socks")
@RequestMapping("/api/socks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Система управления складом с носками", description = "Регистрирует приход и отпуск носков, " +
        "получение общее кол-во носков на складе, обновления склада, а также загрузка партий носков из Excel")
public class WarehouseSocksController {

    private final WarehouseSocksService warehouseSocksService;

    /**
     * Получение общего кол-ва носков с использованием фильтров
     *
     * @param color                        Цвет
     * @param percentageCotton             Процентное содержания хлопка
     * @param operationForPercentageCotton Операция сравнения процентного содержания хлопка
     * @return Кол-во носков
     */
    @Operation(summary = "Регистрация прихода носков на склад")
    @GetMapping()
    public Integer getQuantitySocks(@Parameter(description = "Цвет")
                                    @NotBlank(message = "Цвет должен быть не null и не пустым")
                                    @RequestParam String color,
                                    @Parameter(description = "Оператор сравнения: moreThan - Больше, чем, " +
                                            "lessThan - Меньше, чем и equals - Равен.")
                                    @RequestParam(name = "operation", required = false)
                                    OperationForPercentageCotton operationForPercentageCotton,
                                    @Parameter(description = "Процентное содержания хлопка")
                                    @RequestParam(required = false)
                                    @Max(value = 100, message = "Процент содержания хлопка не может превышать 100%")
                                    @Min(value = 0, message = "Процент содержания хлопка не может быть меньше 0%")
                                    Integer percentageCotton,
                                    @Parameter(description = "Диапазон процентного содержания хлопка",
                                            example = "0-100")
                                    @RequestParam(name = "range", required = false) String rangePercentageCotton) {

        return warehouseSocksService.getQuantitySocksOnWarehouseByFilter(color, percentageCotton,
                operationForPercentageCotton, rangePercentageCotton);
    }

    /**
     * Регистрация прихода носков на склад
     *
     * @param warehouseSocksDto ДТО носков
     * @return Остаток на складе
     */
    @Operation(summary = "Регистрация прихода носков на склад")
    @PostMapping("/income")
    public WarehouseSocksResponseDto income(@RequestBody @Valid WarehouseSocksRequestDto warehouseSocksDto) {
        return warehouseSocksService.income(warehouseSocksDto);
    }

    /**
     * Регистрация отпуска носков на склад
     *
     * @param warehouseSocksDto ДТО носков
     * @return Остаток на складе
     */
    @Operation(summary = "Регистрация отпуска носков на склад")
    @PostMapping("/outcome")
    public WarehouseSocksResponseDto outcome(@RequestBody @Valid WarehouseSocksRequestDto warehouseSocksDto) {
        return warehouseSocksService.outcome(warehouseSocksDto);
    }

    /**
     * Редактирования данных носков на складе
     *
     * @param warehouseSocksDto ДТО носков
     * @return Остаток на складе
     */
    @Operation(summary = "Редактирования данных носков на складе")
    @PutMapping("/{warehouseSocksId}")
    public WarehouseSocksResponseDto update(@RequestBody @Valid WarehouseSocksRequestDto warehouseSocksDto,
                                            @Parameter(description = "Идентификатор носков")
                                            @PathVariable @Min(value = 1, message = "Id должен быть больше нуля")
                                            Long warehouseSocksId) {
        return warehouseSocksService.update(warehouseSocksId, warehouseSocksDto);
    }

    /**
     * Загрузка партии носков из Excel
     *
     * @param file Excel файл
     * @return Остаток на складе
     */
    @Operation(summary = "Загрузка партии носков из Excel")
    @PostMapping("/batch")
    public List<WarehouseSocksResponseDto> uploadFile(@Parameter(description = "Excel файл")
                                                      MultipartFile file) throws IOException {
        return warehouseSocksService.incomeByExcel(file);
    }

}

package com.test.inventorysockswarehouse.model.dto.warehouse_socks;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Возвращаемое ДТО склад с носками")
public class WarehouseSocksResponseDto {

    /**
     * Идентификатор
     */
    @Schema(description = "Идентификатор", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    /**
     * Цвет
     */
    @Schema(description = "Цвет", requiredMode = Schema.RequiredMode.REQUIRED)
    private String color;

    /**
     * Процент содержания хлопка
     */
    @Schema(description = "Процент содержания хлопка", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer percentageCotton;

    /**
     * Кол-во
     */
    @Schema(description = "Кол-во", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;
}

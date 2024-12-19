package com.test.inventorysockswarehouse.model.dto.warehouse_socks;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Получаемое ДТО склад с носками")
public class WarehouseSocksRequestDto {

    /**
     * Цвет
     */
    @NotBlank(message = "Цвет должен быть не null и не пустым")
    @Schema(description = "Цвет")
    @NonNull
    private String color;

    /**
     * Процент содержания хлопка
     */
    @Max(value = 100, message = "Процент содержания хлопка не может превышать 100%")
    @Min(value = 0, message = "Процент содержания хлопка не может быть меньше 0%")
    @Schema(description = "Процент содержания хлопка")
    @NonNull
    private Integer percentageCotton;

    /**
     * Кол-во
     */
    @NotBlank(message = "Кол-во не может быть null или пустым")
    @Min(value = 1, message = "Кол-во не может быть меньше 1")
    @Schema(description = "Кол-во")
    @NonNull
    private Integer quantity;
}

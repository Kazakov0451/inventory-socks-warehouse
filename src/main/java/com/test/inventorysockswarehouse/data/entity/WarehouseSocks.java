package com.test.inventorysockswarehouse.data.entity;

import com.test.inventorysockswarehouse.data.entity.editable_interface.EditableUpdate;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "warehouse_socks", schema = "public")
public class WarehouseSocks implements EditableUpdate<WarehouseSocks> {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата создания
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Дата обновления
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Цвет
     */
    @Column(name = "color")
    private String color;

    /**
     * Процентное содержания хлопка
     */
    @Column(name = "percentage_cotton")
    private Integer percentageCotton;

    /**
     * Количество
     */
    @Column(name = "quantity")
    private Integer quantity;

    @PrePersist
    private void setCreatedFields() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WarehouseSocks update(WarehouseSocks updated) {
        return WarehouseSocks.builder()
                .id(getId())
                .createdAt(getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .color(updated.getColor())
                .percentageCotton(updated.getPercentageCotton())
                .quantity(updated.getQuantity())
                .build();
    }
}

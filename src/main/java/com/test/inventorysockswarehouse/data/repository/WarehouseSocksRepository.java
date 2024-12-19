package com.test.inventorysockswarehouse.data.repository;

import com.test.inventorysockswarehouse.data.entity.WarehouseSocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseSocksRepository extends JpaRepository<WarehouseSocks, Long> {


    /**
     * Получаем склад с носками по цвету носков и по процентному содержанию хлопка
     * @param color Цвет
     * @param percentageCotton Процент содержащего хлопка
     * @return Склад с носками
     */
    Optional<WarehouseSocks> findByColorAndPercentageCotton(String color, Integer percentageCotton);
}

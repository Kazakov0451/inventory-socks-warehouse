package com.test.inventorysockswarehouse;

import com.test.inventorysockswarehouse.converter.WarehouseSocksConverter;
import com.test.inventorysockswarehouse.data.entity.WarehouseSocks;
import com.test.inventorysockswarehouse.data.repository.WarehouseSocksRepository;
import com.test.inventorysockswarehouse.exception.InsufficientQuantityInWarehouseException;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksRequestDto;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksResponseDto;
import com.test.inventorysockswarehouse.service.warehouse_socks.WarehouseSocksServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class WarehouseSocksServiceTest {

    @Mock
    private WarehouseSocksRepository warehouseSocksRepository;
    @Mock
    private WarehouseSocksConverter warehouseSocksConverter;

    @InjectMocks
    private WarehouseSocksServiceImpl warehouseSocksService;

    private WarehouseSocks socks1;

    @BeforeEach
    void beforeEach() {
        socks1 = WarehouseSocks.builder()
                .id(1L)
                .color("Черный")
                .percentageCotton(80)
                .quantity(20)
                .build();
    }

    @Test
    void incomeExistSocksTest() {

        final var warehouseSocksRequestDto = WarehouseSocksRequestDto.builder()
                .color("Черный")
                .percentageCotton(80)
                .quantity(5)
                .build();

        final var warehouseSocksResponseDto = WarehouseSocksResponseDto.builder()
                .id(1L)
                .color("Черный")
                .percentageCotton(80)
                .quantity(25)
                .build();

        when(warehouseSocksRepository.findByColorAndPercentageCotton(anyString(), anyInt()))
                .thenReturn(Optional.ofNullable(socks1));
        socks1.setQuantity(25);
        when(warehouseSocksRepository.save(socks1)).thenReturn(socks1);
        when(warehouseSocksConverter.toDto(socks1)).thenReturn(warehouseSocksResponseDto);

        final var warehouseSocksRes = warehouseSocksService.income(warehouseSocksRequestDto);

        verify(warehouseSocksRepository).findByColorAndPercentageCotton("Черный", 80);
        verify(warehouseSocksRepository).save(socks1);
        verify(warehouseSocksConverter).toDto(socks1);

        assertEquals(warehouseSocksRes.getId(), 1L);
        assertEquals(warehouseSocksRes.getColor(), "Черный");
        assertEquals(warehouseSocksRes.getPercentageCotton(), 80);
        assertEquals(warehouseSocksRes.getQuantity(), 25);

    }

    @Test
    void incomeNotExistSocksTest() {
        final var warehouseSocksRequestDto = WarehouseSocksRequestDto.builder()
                .color("Черный")
                .percentageCotton(75)
                .quantity(5)
                .build();

        final WarehouseSocks socks3 = WarehouseSocks.builder()
                .color("Черный")
                .percentageCotton(75)
                .quantity(5)
                .build();

        final var warehouseSocksResponseDto = WarehouseSocksResponseDto.builder()
                .id(3L)
                .color("Черный")
                .percentageCotton(75)
                .quantity(5)
                .build();

        when(warehouseSocksConverter.toEntity(warehouseSocksRequestDto)).thenReturn(socks3);
        when(warehouseSocksRepository.save(socks3)).thenReturn(socks3);
        when(warehouseSocksConverter.toDto(socks3)).thenReturn(warehouseSocksResponseDto);

        final var warehouseSocksRes = warehouseSocksService.income(warehouseSocksRequestDto);

        verify(warehouseSocksRepository).findByColorAndPercentageCotton(anyString(), anyInt());
        verify(warehouseSocksRepository).save(socks3);
        verify(warehouseSocksConverter).toDto(socks3);

        assertEquals(warehouseSocksRes.getId(), 3L);
        assertEquals(warehouseSocksRes.getColor(), "Черный");
        assertEquals(warehouseSocksRes.getPercentageCotton(), 75);
        assertEquals(warehouseSocksRes.getQuantity(), 5);
    }

    @Test
    void outcomeTest() {
        final var warehouseSocksRequestDto = WarehouseSocksRequestDto.builder()
                .color("Черный")
                .percentageCotton(80)
                .quantity(5)
                .build();

        final var warehouseSocksResponseDto = WarehouseSocksResponseDto.builder()
                .id(1L)
                .color("Черный")
                .percentageCotton(80)
                .quantity(15)
                .build();

        when(warehouseSocksRepository.findByColorAndPercentageCotton(anyString(), anyInt()))
                .thenReturn(Optional.ofNullable(socks1));
        socks1.setQuantity(socks1.getQuantity() - warehouseSocksRequestDto.getQuantity());
        when(warehouseSocksRepository.save(socks1)).thenReturn(socks1);
        when(warehouseSocksConverter.toDto(socks1)).thenReturn(warehouseSocksResponseDto);

        final var warehouseSocksRes = warehouseSocksService.outcome(warehouseSocksRequestDto);

        verify(warehouseSocksRepository).findByColorAndPercentageCotton(anyString(), anyInt());
        verify(warehouseSocksRepository).save(socks1);
        verify(warehouseSocksConverter).toDto(socks1);

        assertEquals(warehouseSocksRes.getId(), 1L);
        assertEquals(warehouseSocksRes.getColor(), "Черный");
        assertEquals(warehouseSocksRes.getPercentageCotton(), 80);
        assertEquals(warehouseSocksRes.getQuantity(), 15);

    }

    @Test
    void outcomeInsufficientQuantityInWarehouseExceptionTest() {
        final var warehouseSocksRequestDto = WarehouseSocksRequestDto.builder()
                .color("Черный")
                .percentageCotton(80)
                .quantity(25)
                .build();

        when(warehouseSocksRepository.findByColorAndPercentageCotton(anyString(), anyInt()))
                .thenThrow(new InsufficientQuantityInWarehouseException("На складе недостаточно носков для отпуска"));

        assertThrows(InsufficientQuantityInWarehouseException.class,
                () -> warehouseSocksService.outcome(warehouseSocksRequestDto));

        verify(warehouseSocksRepository).findByColorAndPercentageCotton(anyString(), anyInt());
    }

}

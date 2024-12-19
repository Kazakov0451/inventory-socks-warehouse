package com.test.inventorysockswarehouse.service.warehouse_socks;

import com.test.inventorysockswarehouse.converter.WarehouseSocksConverter;
import com.test.inventorysockswarehouse.data.entity.WarehouseSocks;
import com.test.inventorysockswarehouse.data.repository.WarehouseSocksRepository;
import com.test.inventorysockswarehouse.exception.GenericNotFoundException;
import com.test.inventorysockswarehouse.exception.IncorrectFileException;
import com.test.inventorysockswarehouse.exception.InsufficientQuantityInWarehouseException;
import com.test.inventorysockswarehouse.model.dto.enums.OperationForPercentageCotton;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksRequestDto;
import com.test.inventorysockswarehouse.model.dto.warehouse_socks.WarehouseSocksResponseDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseSocksServiceImpl implements WarehouseSocksService {

    private final EntityManager entityManager;
    private final WarehouseSocksRepository warehouseSocksRepository;
    private final WarehouseSocksConverter warehouseSocksConverter;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public WarehouseSocksResponseDto income(WarehouseSocksRequestDto warehouseSocksDto) {
        final var warehouseSocks = warehouseSocksRepository.findByColorAndPercentageCotton(warehouseSocksDto.getColor(),
                warehouseSocksDto.getPercentageCotton()).orElse(null);

        if (warehouseSocks != null) {
            warehouseSocks.setQuantity(warehouseSocks.getQuantity() + warehouseSocksDto.getQuantity());
            final var warehouseSocksUpdated = warehouseSocksRepository.save(warehouseSocks);

            log.info("На складе существуют такие носки. Обновилась существующая сущность в базе данных");

            return warehouseSocksConverter.toDto(warehouseSocksUpdated);
        } else {
            final var warehouseSockCreated = warehouseSocksRepository
                    .save(warehouseSocksConverter.toEntity(warehouseSocksDto));

            log.info("На складе нету таких носков. Создана новая сущность в базе данных");

            return warehouseSocksConverter.toDto(warehouseSockCreated);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WarehouseSocksResponseDto outcome(WarehouseSocksRequestDto warehouseSocksDto) {
        final var warehouseSocks = warehouseSocksRepository
                .findByColorAndPercentageCotton(warehouseSocksDto.getColor(), warehouseSocksDto.getPercentageCotton())
                .orElseThrow(() -> new GenericNotFoundException(warehouseSocksDto.getColor(),
                        warehouseSocksDto.getPercentageCotton(), WarehouseSocks.class));

        if (warehouseSocks.getQuantity() > warehouseSocksDto.getQuantity()) {
            warehouseSocks.setQuantity(warehouseSocks.getQuantity() - warehouseSocksDto.getQuantity());
            final var warehouseSocksUpdated = warehouseSocksRepository.save(warehouseSocks);

            log.info("На складе существуют такие носки и в таком кол-ве. Сущность обновлена.");

            return warehouseSocksConverter.toDto(warehouseSocksUpdated);
        } else {
            throw new InsufficientQuantityInWarehouseException("На складе недостаточно носков для отпуска");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getQuantitySocksOnWarehouseByFilter(String color, Integer percentageCotton,
                                                       OperationForPercentageCotton operationForPercentageCotton,
                                                       String rangePercentageCotton) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer> criteriaQuery = builder.createQuery(Integer.class);
        Root<WarehouseSocks> root = criteriaQuery.from(WarehouseSocks.class);

        List<Predicate> predicates = new ArrayList<>();

        if (color != null) {
            predicates.add(builder.equal(root.get("color"), color));
        }

        if (rangePercentageCotton != null && operationForPercentageCotton == null) {
            final var percentageCottonStart = Integer.parseInt(rangePercentageCotton.split("-")[0]);
            final var percentageCottonEnd = Integer.parseInt(rangePercentageCotton.split("-")[1]);

            predicates.add(
                    builder.and(
                            builder.or(builder.gt(root.get("percentageCotton"), percentageCottonStart),
                                    builder.equal(root.get("percentageCotton"), percentageCottonStart)),
                            builder.or(builder.lt(root.get("percentageCotton"), percentageCottonEnd),
                                    builder.equal(root.get("percentageCotton"), percentageCottonEnd))
                    )
            );
        }

        if (percentageCotton != null && operationForPercentageCotton != null) {
            switch (operationForPercentageCotton) {
                case equal -> predicates.add(builder.equal(root.get("percentageCotton"), percentageCotton));
                case lessThan -> predicates.add(builder.lt(root.get("percentageCotton"), percentageCotton));
                case moreThan -> predicates.add(builder.gt(root.get("percentageCotton"), percentageCotton));
            }
        }

        criteriaQuery.select(builder.sum(root.get("quantity"))).where(predicates.toArray(Predicate[]::new));
        Integer result = entityManager.createQuery(criteriaQuery).getSingleResult();
        result = (result != null) ? result : 0;

        log.info("Успешно выполнен запрос на получение общего кол-ва носков на складе по фильтру.");

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WarehouseSocksResponseDto update(Long warehouseSocksId, WarehouseSocksRequestDto warehouseSocksDto) {
        final var warehouseSocks = warehouseSocksRepository.findById(warehouseSocksId)
                .orElseThrow(() -> new GenericNotFoundException(warehouseSocksId, WarehouseSocks.class));

        final var warehouseSocksUpdated = warehouseSocksRepository
                .save(warehouseSocks.update(warehouseSocksConverter.toEntity(warehouseSocksDto)));

        log.info("Сущность успешно обновилась.");

        return warehouseSocksConverter.toDto(warehouseSocksUpdated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WarehouseSocksResponseDto> incomeByExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<WarehouseSocks> warehouseSocksList = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String color = row.getCell(0).getStringCellValue();
                int percentageCotton = (int) row.getCell(1).getNumericCellValue();
                int quantity = (int) row.getCell(2).getNumericCellValue();

                final var warehouseSocks = warehouseSocksRepository.findByColorAndPercentageCotton(color,
                        percentageCotton).orElse(null);

                if (warehouseSocks != null) {
                    warehouseSocks.setQuantity(warehouseSocks.getQuantity() + quantity);
                    warehouseSocksList.add(warehouseSocks);

                } else {
                    var warehouseSocksNew = WarehouseSocks.builder()
                            .color(color)
                            .percentageCotton(percentageCotton)
                            .quantity(quantity)
                            .build();

                    warehouseSocksList.add(warehouseSocksNew);
                }
            }

            final var warehouseSocksUpdatedOrCreatedList = warehouseSocksRepository.saveAll(warehouseSocksList);

            log.info("Успешно добавились или обновились сущности из новой партии файла Excel.");

            return warehouseSocksUpdatedOrCreatedList.stream()
                    .map(warehouseSocksConverter::toDto)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            throw new IncorrectFileException("Проверьте правильность заполнения файла. "
                    + "Файл должен быть с расширением .xlsx/.xls");
        }
    }
}

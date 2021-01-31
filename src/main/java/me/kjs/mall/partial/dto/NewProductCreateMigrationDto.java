package me.kjs.mall.partial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.Product;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductCreateMigrationDto {
    private Product product;
    private LocalDate beginDate;
    private LocalDate endDate;
}

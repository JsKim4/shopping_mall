package me.kjs.mall.partial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductCreateDto {
    @NotNull
    private Long productId;
    @NotNull
    private LocalDate beginDate;
    @NotNull
    private LocalDate endDate;
}

package me.kjs.mall.point.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointGiveDto {
    @Min(1)
    @Max(5000)
    private int amount;
    @NotEmpty
    @Length(min = 10, max = 150)
    private String misc;
    @NotNull
    @Min(1)
    private Long memberId;
    private LocalDate expiredDate;
}

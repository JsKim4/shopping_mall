package me.kjs.mall.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointSpecificIdAndAmountDto {
    private Long id;
    private int amount;
}

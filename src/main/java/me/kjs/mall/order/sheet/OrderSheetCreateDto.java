package me.kjs.mall.order.sheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSheetCreateDto {
    @NotNull
    @NotEmpty
    @Valid
    private List<@NotNull ProductIdAndQuantityDto> products;
}

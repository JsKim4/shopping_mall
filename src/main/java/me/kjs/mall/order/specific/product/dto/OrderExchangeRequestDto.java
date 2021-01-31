package me.kjs.mall.order.specific.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderExchangeRequestDto {
    @NotBlank
    private List<@NotBlank String> causeImage;
    @NotBlank
    private String exchangeCause;
    @NotBlank
    private List<@NotNull OrderProductIdAndQuantityDto> ordersProducts;
}

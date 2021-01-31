package me.kjs.mall.product.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.product.dto.ProductDeliveryDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductSaveDto {
    @NotBlank
    private String code;
    @NotBlank
    private String name;
    @NotNull
    private ProductDeliveryDto productDelivery;
    @Builder.Default
    @Size(max = 10)
    private List<@NotBlank String> tags = new ArrayList<>();
    private String contents;
    private List<@NotBlank String> thumbnail;
    private int originPrice;
}

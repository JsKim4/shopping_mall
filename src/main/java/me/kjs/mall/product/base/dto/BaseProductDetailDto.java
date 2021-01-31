package me.kjs.mall.product.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.dto.ProductDeliveryDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseProductDetailDto {
    private Long baseProductId;
    private String name;
    private String code;
    private int originPrice;
    private CommonStatus status;
    private ProductDeliveryDto productDelivery;
    private List<String> tags;
    private String contents;
    private List<String> thumbnail;
    private ProvisionNoticeDto provisionNotice;

    public static BaseProductDetailDto baseProductToDetailDto(BaseProduct baseProduct) {
        return BaseProductDetailDto.builder()
                .baseProductId(baseProduct.getId())
                .name(baseProduct.getName())
                .code(baseProduct.getCode())
                .originPrice(baseProduct.getOriginPrice())
                .status(baseProduct.getStatus())
                .productDelivery(ProductDeliveryDto.productDeliveryToDto(baseProduct.getProductDelivery()))
                .tags(baseProduct.getProductTags())
                .contents(baseProduct.getContent())
                .thumbnail(baseProduct.getThumbnailImageUrls())
                .provisionNotice(ProvisionNoticeDto.provisionNoticeToDto(baseProduct.getProvisionNotice()))
                .build();
    }
}

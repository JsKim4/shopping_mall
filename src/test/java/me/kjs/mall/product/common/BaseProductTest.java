package me.kjs.mall.product.common;

import me.kjs.mall.common.type.CommonStatus;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.product.base.BaseProduct;
import me.kjs.mall.product.base.dto.BaseProductSaveDto;
import me.kjs.mall.product.base.dto.ImageAndOrder;
import me.kjs.mall.product.dto.ProductDeliveryDto;
import me.kjs.mall.product.type.DeliveryType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseProductTest {

    @Test
    @DisplayName("상품 기본 정보 생성")
    void test() {
        List<String> tags = Arrays.asList("태그1", "태그2", "태그3");

        ProductDeliveryDto productDeliveryDto = ProductDeliveryDto.builder()
                .returnLocation("RETURN LOCATION")
                .feeCondition(0)
                .fee(0)
                .deliveryType(DeliveryType.FREE)
                .deliveryYn(YnType.Y)
                .bundleYn(YnType.Y)
                .build();

        List<String> images = new ArrayList<>();
        images.add("/image/image001.png");
        images.add("/image/image002.png");
        images.add("/image/image003.png");
        images.add("/image/image004.png");
        images.add("/image/image005.png");

        BaseProductSaveDto baseProductSaveDto = BaseProductSaveDto.builder()
                .tags(tags)
                .productDelivery(productDeliveryDto)
                .name("PRODUCT NAME")
                .code("A000001")
                .contents(images.toString())
                .thumbnail(Arrays.asList("/image/thumbnail1.png", "/image/thumbnail2.png"))
                .build();

        BaseProduct baseProduct = BaseProduct.createBaseProduct(baseProductSaveDto);

        assertEquals(baseProduct.getCode(), baseProductSaveDto.getCode());
        assertEquals(baseProduct.getName(), baseProductSaveDto.getName());

        for (ImageAndOrder imageAndOrder : baseProduct.getThumbnailImage()) {
            assertTrue(baseProductSaveDto.getThumbnail().contains(imageAndOrder.getImage()));
        }
        assertEquals(baseProduct.getStatus(), CommonStatus.CREATED);
        assertEquals(baseProduct.getProductDelivery().getDeliveryYn(), baseProductSaveDto.getProductDelivery().getDeliveryYn());
        assertEquals(baseProduct.getProductDelivery().getBundleYn(), baseProductSaveDto.getProductDelivery().getBundleYn());
        assertEquals(baseProduct.getProductDelivery().getDeliveryType(), baseProductSaveDto.getProductDelivery().getDeliveryType());
        assertEquals(baseProduct.getProductDelivery().getReturnLocation(), baseProductSaveDto.getProductDelivery().getReturnLocation());
        assertEquals(baseProduct.getProductDelivery().getFee(), baseProductSaveDto.getProductDelivery().getFee());
        assertEquals(baseProduct.getProductDelivery().getFeeCondition(), baseProductSaveDto.getProductDelivery().getFeeCondition());

        for (String productTag : baseProduct.getProductTags()) {
            assertTrue(baseProductSaveDto.getTags().stream().anyMatch(p -> p.equals(productTag)));
        }


    }

}
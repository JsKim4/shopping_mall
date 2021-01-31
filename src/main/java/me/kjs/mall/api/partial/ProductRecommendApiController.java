package me.kjs.mall.api.partial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.type.CurrentMember;
import me.kjs.mall.member.Member;
import me.kjs.mall.partial.BestSeller;
import me.kjs.mall.partial.BestSellerQueryRepository;
import me.kjs.mall.partial.ProductRecommendRepository;
import me.kjs.mall.product.dto.ProductSimpleDto;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/products/recommend")
public class ProductRecommendApiController {


    private final BestSellerQueryRepository bestSellerQueryRepository;

    private final ProductRecommendRepository productRecommendRepository;

    @GetMapping
    public ResponseDto queryProductRecommend(@CurrentMember Member member, @Validated CommonSearchCondition commonSearchCondition, Errors errors) {
        hasErrorsThrow(errors);
        if (member == null) {
            CommonPage<BestSeller> bestSeller = bestSellerQueryRepository.findBestSellerByMaxDateAndSearchCondition(commonSearchCondition);
            CommonPage body = bestSeller.updateContent(bestSeller.getContents().stream().map(BestSeller::getProduct).map(ProductSimpleDto::productToSimpleDto).collect(Collectors.toList()));
            return ResponseDto.ok(body);
        }
        CommonPage<ProductSimpleDto> products = productRecommendRepository.findRecommendProductBySearchCondition(commonSearchCondition, member);
        return ResponseDto.ok(products);
    }
}

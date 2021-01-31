package me.kjs.mall.api.partial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.CommonSearchCondition;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.partial.BestSeller;
import me.kjs.mall.partial.BestSellerQueryRepository;
import me.kjs.mall.product.dto.ProductSimpleDto;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

import static me.kjs.mall.common.util.ThrowUtil.hasErrorsThrow;

@RestController
@RequestMapping("/api/best/sellers")
@RequiredArgsConstructor
@Slf4j
public class BestSellerApiController {

    private final BestSellerQueryRepository bestSellerQueryRepository;

    @GetMapping
    public ResponseDto queryBestSeller(@Validated CommonSearchCondition commonSearchCondition,
                                       Errors errors) {
        hasErrorsThrow(errors);
        CommonPage<BestSeller> bestSeller = bestSellerQueryRepository.findBestSellerByMaxDateAndSearchCondition(commonSearchCondition);
        CommonPage body = bestSeller.updateContent(bestSeller.getContents().stream().map(BestSeller::getProduct).map(ProductSimpleDto::productToSimpleDto).collect(Collectors.toList()));
        return ResponseDto.ok(body);
    }
}

package me.kjs.mall.api.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.dto.OnlyIdsDto;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.order.OrderAdminService;
import me.kjs.mall.order.OrderQueryRepository;
import me.kjs.mall.order.dto.OrderAdminSearchCondition;
import me.kjs.mall.order.dto.OrderProductDetailOnAdminDto;
import me.kjs.mall.order.dto.OrderProductSimpleOnAdminDto;
import me.kjs.mall.order.dto.PlaceOrderDownloadForErpResultDto;
import me.kjs.mall.order.specific.product.OrderProduct;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ROLE_ORDER')")
public class OrderAdminApiController {

    private final OrderQueryRepository orderQueryRepository;

    private final OrderAdminService orderAdminService;


    @GetMapping
    public ResponseDto orderProductsQueryByAdminSearchCondition(OrderAdminSearchCondition orderAdminSearchCondition) {
        CommonPage<OrderProduct> result = orderQueryRepository.findOrderProductByAdminSearchCondition(orderAdminSearchCondition);
        CommonPage body = result.updateContent(result.getContents().stream().map(OrderProductSimpleOnAdminDto::orderProductToSimpleOnAdminDto).collect(Collectors.toList()));
        return ResponseDto.ok(body);
    }

    @GetMapping("/{orderProductId}")
    public ResponseDto orderProductQuery(@PathVariable("orderProductId") Long orderProductId) {
        OrderProductDetailOnAdminDto body = orderAdminService.findOrderProductDetailOnAdminDto(orderProductId);
        return ResponseDto.ok(body);
    }

    @PostMapping("/place/download")
    public ResponseDto createPlaceOrderDownloadLogByOrderSpecificIds(@RequestBody OnlyIdsDto onlyIds) {
        PlaceOrderDownloadForErpResultDto placeOrderForErp = orderAdminService.findPlaceOrderForErp(onlyIds);
        orderAdminService.createPlaceOrderLog(onlyIds);
        return ResponseDto.created(placeOrderForErp);
    }

    @PutMapping("/place/accept/{orderSpecificId}")
    public ResponseDto orderSetDeliveryWait(@PathVariable("orderSpecificId") Long orderSpecificId) {
        orderAdminService.placeAccept(orderSpecificId);
        return ResponseDto.noContent();
    }

}

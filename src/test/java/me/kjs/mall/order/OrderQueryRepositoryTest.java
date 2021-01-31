package me.kjs.mall.order;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.dto.CommonPage;
import me.kjs.mall.common.exception.NoExistIdException;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.dto.sign.TokenDto;
import me.kjs.mall.member.exception.login.NotFoundMemberByRefreshTokenException;
import me.kjs.mall.order.common.OrderState;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.OrderAdminSearchCondition;
import me.kjs.mall.order.dto.OrderSearchConditionDto;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.specific.OrderSpecific;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.order.specific.product.OrderProduct;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


class OrderQueryRepositoryTest extends BaseTest {
    @Test
    void test() {
        TokenDto tokenDto = getTokenDto();
        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NotFoundMemberByRefreshTokenException::new);
        OrderSearchConditionDto orderSearchConditionDto = OrderSearchConditionDto.builder()
                .orderState(OrderState.ORDER_CREATE)
                .contents(10)
                .page(0)
                .build();

        List<OrderSpecific> orderByMemberAndCondition = orderQueryRepository.findOrderByMemberAndCondition(orderSearchConditionDto, member);
        for (OrderSpecific orderSpecific : orderByMemberAndCondition) {
        }
    }

    @Test
    void orderAdminQueryTest() {

        OrderCreateDto orderCreateDto = generateOrderCreateDto();


        TokenDto tokenDto = getUserTokenDto();

        Member member = memberRepository.findByRefreshToken(tokenDto.getRefreshToken()).orElseThrow(NoExistIdException::new);
        Order order = orderService.createOrder(orderCreateDto, member.getId());
        em.flush();
        em.clear();
        OrderAdminSearchCondition orderAdminSearchCondition = OrderAdminSearchCondition.builder()
                .contents(10)
                .page(0)
                .build();
        CommonPage<OrderProduct> orderProductByAdminSearchCondition = orderQueryRepository.findOrderProductByAdminSearchCondition(orderAdminSearchCondition);
    }

    private OrderCreateDto generateOrderCreateDto() {
        List<Product> allProducts = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Product> products = AvailableUtil.isUsedFilter(allProducts);
        List<Product> result = products.stream().collect(Collectors.toList());
        List<ProductIdAndQuantityDto> productIdAndQuantityDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Product product = result.get(i);
            ProductIdAndQuantityDto productIdAndQuantityDto = ProductIdAndQuantityDto.builder()
                    .productId(product.getId())
                    .quantity(i + 1)
                    .build();
            productIdAndQuantityDtos.add(productIdAndQuantityDto);
        }

        return OrderCreateDto.builder()
                .destination(Arrays.asList(
                        OrderDestinationSaveDto.builder()
                                .recipient("recipient1")
                                .addressSimple("address1")
                                .addressDetail("addressDetail1")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("40152")
                                .message("ismessage")
                                .build()
                ))
                .orderMultipleYn(YnType.Y)
                .paymentMethod(PaymentMethod.CARD)
                .products(productIdAndQuantityDtos)
                .build();
    }


}
package me.kjs.mall.order;

import me.kjs.mall.common.BaseTest;
import me.kjs.mall.common.type.YnType;
import me.kjs.mall.common.util.AvailableUtil;
import me.kjs.mall.member.Member;
import me.kjs.mall.member.MemberRepository;
import me.kjs.mall.member.type.AccountStatus;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.dto.create.OrderCreateDto;
import me.kjs.mall.order.dto.create.ProductIdAndQuantityDto;
import me.kjs.mall.order.exception.NotAvailableOrderAccountStatusException;
import me.kjs.mall.order.specific.destination.dto.OrderDestinationSaveDto;
import me.kjs.mall.product.Product;
import me.kjs.mall.product.dto.ProductSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class OrderServiceTest extends BaseTest {

    @MockBean
    private MemberRepository mockMemberRepository;

    @Test
    void orderFailByAccountStatusLimit() {

        Member member = Member.builder()
                .accountStatus(AccountStatus.BAN)
                .build();

        given(mockMemberRepository.findById(0L)).willReturn(Optional.ofNullable(member));
        List<Product> allProducts = productQueryRepository.findProductsBySearchCondition(ProductSearchCondition.builder().build()).getContents();
        List<Product> products = AvailableUtil.isUsedFilter(allProducts);
        List<Product> result = products.stream().collect(Collectors.toList());
        List<ProductIdAndQuantityDto> productIdAndQuantityDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProductIdAndQuantityDto build = ProductIdAndQuantityDto.builder()
                    .productId(result.get(i).getId())
                    .quantity(2)
                    .build();
            productIdAndQuantityDtos.add(build);
        }
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .destination(Arrays.asList(
                        OrderDestinationSaveDto
                                .builder()
                                .addressSimple("주소1")
                                .addressDetail("주소상세")
                                .message("주문 메시지")
                                .recipient("수령인")
                                .tel1("01099998888")
                                .tel2("01077778888")
                                .zipcode("45612")
                                .build()))
                .orderMultipleYn(YnType.N)
                .products(productIdAndQuantityDtos)
                .paymentMethod(PaymentMethod.CARD)
                .build();
        NotAvailableOrderAccountStatusException notAvailableOrderAccountStatusException = assertThrows(NotAvailableOrderAccountStatusException.class, () -> {
            orderService.createOrder(orderCreateDto, 0L);
        });
    }

}
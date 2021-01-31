package me.kjs.mall.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kjs.mall.common.dto.ResponseDto;
import me.kjs.mall.common.util.CodeGeneratorUtil;
import me.kjs.mall.order.common.PaymentMethod;
import me.kjs.mall.order.payment.dto.OrderPaymentDetailDto;
import me.kjs.mall.order.payment.dto.OrderPaymentResultDto;
import me.kjs.mall.order.payment.dto.OrderPaymentVirtualBankResultDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;


@RestController
public class PaymentInterfaceController {

    @GetMapping("/test/payment")
    private ResponseDto getPaymentSuccess() {
        OrderPaymentResultDto resultDto = OrderPaymentResultDto.builder()
                .orderCodes(Arrays.asList(CodeGeneratorUtil.orderSpecificCodeGenerate()))
                .orderSpecificId(Arrays.asList(1L))
                .orderId(1L)
                .virtualBankResult(OrderPaymentVirtualBankResultDto.builder()
                        .bankCode("004")
                        .bankExpiredDateTime(LocalDateTime.of(2021, 01, 24, 1, 1))
                        .bankName("KB")
                        .bankNum("018301-04-182222")
                        .build())
                .orderPaymentDetail(OrderPaymentDetailDto.builder()
                        .paymentDateTime(LocalDateTime.of(2021, 01, 21, 1, 1))
                        .paymentMethod(PaymentMethod.VBANK)
                        .paymentPrice(37200)
                        .build())
                .build();
        return ResponseDto.ok(resultDto);
    }

    @PostMapping("/test/payment/orderForm")
    private String createOrderFormDto(@RequestBody CreateOrderFormDto createOrderFormDto) {
        return "";
    }


    @NoArgsConstructor
    @Getter
    public static class CreateOrderFormDto {
        private String goodsName = "goodsName";
        private int amt = 27000;
        private String payMethod = "CARD";
        private String signData = "dasdasjdkfgsdjhfgjkdfskld";
        private String mid = "nicepay00m";
        private String ediDate = "20201120164605";
        private String moid = "P20201120164557";
        private String returnUrl = "[https]://[api-host]:[port]/[url]";
        private String certificationRequestUrl = "[https]://[payment-host]:[port]/[url]";
        private String vbankExpDate = "202101191120";
    }
}
